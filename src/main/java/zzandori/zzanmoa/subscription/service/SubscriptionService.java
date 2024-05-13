package zzandori.zzanmoa.subscription.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
        StringBuilder resultMessage = new StringBuilder();
        boolean allSavedSuccessfully = true;

        for (String districtName : subscriptionDto.getDistrict()) {
            ResponseEntity<?> response = processSubscriptionForDistrict(subscriptionDto, districtName);
            if (!response.getStatusCode().is2xxSuccessful()) {
                allSavedSuccessfully = false;
                resultMessage.append((String) response.getBody());
            }
        }

        return allSavedSuccessfully
            ? ResponseEntity.ok("All subscriptions created successfully.")
            : ResponseEntity.badRequest().body(resultMessage.toString());
    }

    private ResponseEntity<?> processSubscriptionForDistrict(SubscriptionDTO subscriptionDto, String districtName) {
        District district = districtRepository.findByDistrictName(districtName);
        if (district == null) {
            return ResponseEntity.badRequest().body("District name '" + districtName + "' not found. ");
        }

        if (alreadySubscribe(subscriptionDto.getEmail(), subscriptionDto.getName(), district)) {
            return ResponseEntity.badRequest().body("Already subscribed to " + districtName + ". ");
        }

        boolean saved = createSubscription(subscriptionDto.getName(), subscriptionDto.getEmail(), district);
        return saved
            ? ResponseEntity.ok("Subscription created successfully for " + districtName + ".")
            : ResponseEntity.badRequest().body("Failed to create subscription for district: " + districtName + ". ");
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
