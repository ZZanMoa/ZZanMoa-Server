package zzandori.zzanmoa.subscription.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.entity.District;
import zzandori.zzanmoa.bargainboard.repository.DistrictRepository;
import zzandori.zzanmoa.subscription.dto.SubscriptionDTO;
import zzandori.zzanmoa.subscription.entity.EmailHistory;
import zzandori.zzanmoa.subscription.entity.Subscription;
import zzandori.zzanmoa.subscription.repository.EmailHistoryRepository;
import zzandori.zzanmoa.subscription.repository.SubscriptionRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    @Value("${MAIL_USERNAME}")
    private String MAIL_USERNAME;

    private final JavaMailSender emailSender;
    private final DistrictRepository districtRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailHistoryRepository emailHistoryRepository;

    @Transactional
    public ResponseEntity<?> subscribe(SubscriptionDTO subscriptionDto) {
        Map<String, Object> results = new LinkedHashMap<>();
        boolean allSuccess = true;

        for (String districtName : subscriptionDto.getDistrict()) {
            ResponseEntity<?> response = processSubscriptionForDistrict(subscriptionDto, districtName);
            if (!response.getStatusCode().is2xxSuccessful()) {
                allSuccess = false;
            }
            results.put(districtName, Map.of(
                "status", response.getStatusCode(),
                "message", Objects.requireNonNull(response.getBody()).toString()
            ));
        }

        HttpStatus overallStatus = allSuccess ? HttpStatus.CREATED : HttpStatus.PARTIAL_CONTENT;
        return new ResponseEntity<>(results, overallStatus);
    }


    private ResponseEntity<?> processSubscriptionForDistrict(SubscriptionDTO subscriptionDto, String districtName) {
        District district = districtRepository.findByDistrictName(districtName);
        if (district == null) {
            return ResponseEntity.badRequest().body("해당 자치구 존재하지 않음");
        }

        if (alreadySubscribe(subscriptionDto.getEmail(), subscriptionDto.getName(), district)) {
            return ResponseEntity.badRequest().body("이미 구독한 내역 존재");
        }

        boolean saved = createSubscription(subscriptionDto.getName(), subscriptionDto.getEmail(), district);
        return saved
            ? ResponseEntity.ok("구독 성공")
            : ResponseEntity.badRequest().body("구독 실패");
    }


    private boolean alreadySubscribe(String email, String name, District district) {
        List<Subscription> subscriptions = subscriptionRepository.findByEmailAndNameAndDistrict(email, name, district);
        return !subscriptions.isEmpty();
    }

    private boolean createSubscription(String name, String email, District district) {
        try {
            Subscription subscription = Subscription.builder()
                .name(name)
                .email(email)
                .district(district)
                .build();
            subscriptionRepository.save(subscription);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void sendEmail(String to, String subject, String text, Subscription subscription, BargainBoard bargainBoard) {
        if (isEmailAlreadySent(subscription, bargainBoard)) {
            return;
        }
        prepareAndSendEmail(to, subject, text, subscription, bargainBoard);
    }

    private void prepareAndSendEmail(String to, String subject, String text, Subscription subscription, BargainBoard bargainBoard) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_USERNAME + "@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            emailSender.send(message);
            saveEmailHistory(subscription, bargainBoard, "SUCCESS");
        } catch (Exception e) {
            saveEmailHistory(subscription, bargainBoard, "FAILED");
        }
    }

    private boolean isEmailAlreadySent(Subscription subscription, BargainBoard bargainBoard) {
        return emailHistoryRepository.existsBySubscriptionAndBargainAndStatus(subscription, bargainBoard, "SUCCESS");
    }

    private void saveEmailHistory(Subscription subscription, BargainBoard bargainBoard, String status) {
        EmailHistory history = EmailHistory.builder()
            .subscription(subscription)
            .bargain(bargainBoard)
            .status(status)
            .sentAt(LocalDateTime.now())
            .build();

        emailHistoryRepository.save(history);
    }
}
