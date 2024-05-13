package zzandori.zzanmoa.subscription.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.entity.District;
import zzandori.zzanmoa.bargainboard.repository.DistrictRepository;
import zzandori.zzanmoa.common.dto.SuccessResponseDTO;
import zzandori.zzanmoa.exception.subscription.DistrictForSubscriptionAppException;
import zzandori.zzanmoa.exception.subscription.SubscriptionAppException;
import zzandori.zzanmoa.exception.subscription.SubscriptionErrorCode;
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
        for (String districtName : subscriptionDto.getDistrict()) {
            processSubscriptionForDistrict(subscriptionDto, districtName);
        }

        return ResponseEntity.ok().body(SuccessResponseDTO.builder()
            .statusCode(HttpStatus.OK.value())
            .message("구독하기 성공")
            .build());
    }


    private void processSubscriptionForDistrict(SubscriptionDTO subscriptionDto, String districtName) {
        District district = districtRepository.findByDistrictName(districtName);
        if (district == null) {
            throw new DistrictForSubscriptionAppException(SubscriptionErrorCode.DISTRICT_NOT_FOUND, districtName);
        }

        if (alreadySubscribe(subscriptionDto.getEmail(), subscriptionDto.getName(), district)) {
            throw new DistrictForSubscriptionAppException(SubscriptionErrorCode.SUBSCRIPTION_DUPLICATED, districtName);
        }

        boolean saved = createSubscription(subscriptionDto.getName(), subscriptionDto.getEmail(), district);
        if (!saved) {
            throw new SubscriptionAppException(SubscriptionErrorCode.SAVE_DATA_FAILED);
        }
    }


    private boolean alreadySubscribe(String email, String name, District district) {
        List<Subscription> subscriptions = subscriptionRepository.findByEmailAndNameAndDistrict(email, name, district);
        return !subscriptions.isEmpty();
    }

    private boolean createSubscription(String name, String email, District district) {
        Subscription subscription = Subscription.builder()
            .name(name)
            .email(email)
            .district(district)
            .build();
        subscriptionRepository.save(subscription);
        return true;
    }

    public void sendEmail(String to, String subject, String text, Subscription subscription, BargainBoard bargainBoard) {
        if (isEmailAlreadySent(subscription, bargainBoard)) {
            return;
        }
        try {
            prepareAndSendEmail(to, subject, text, subscription, bargainBoard);
        } catch (Exception e) {
            throw new SubscriptionAppException(SubscriptionErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private void prepareAndSendEmail(String to, String subject, String text, Subscription subscription, BargainBoard bargainBoard) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_USERNAME + "@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        saveEmailHistory(subscription, bargainBoard, "SUCCESS");
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
