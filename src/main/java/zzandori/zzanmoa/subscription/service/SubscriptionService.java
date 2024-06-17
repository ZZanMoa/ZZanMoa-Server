package zzandori.zzanmoa.subscription.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import zzandori.zzanmoa.common.dto.ApiResponse;
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
        List<String> duplicatedDistricts = new ArrayList<>();
        List<String> notFoundDistricts = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        validateDistricts(subscriptionDto, duplicatedDistricts, notFoundDistricts);

        if (!duplicatedDistricts.isEmpty()) {
            throw new DistrictForSubscriptionAppException(SubscriptionErrorCode.SUBSCRIPTION_DUPLICATED, duplicatedDistricts);
        }

        if (!notFoundDistricts.isEmpty()) {
            throw new DistrictForSubscriptionAppException(SubscriptionErrorCode.DISTRICT_NOT_FOUND, notFoundDistricts);
        }

        processSubscriptions(subscriptionDto, errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new SubscriptionAppException(SubscriptionErrorCode.SAVE_DATA_FAILED);
        }

        return ResponseEntity.ok().body(ApiResponse.builder()
            .statusCode(HttpStatus.OK.value())
            .message("구독하기 성공")
            .build());
    }

    private void validateDistricts(SubscriptionDTO subscriptionDto, List<String> duplicatedDistricts, List<String> notFoundDistricts) {
        for (String districtName : subscriptionDto.getDistrict()) {
            District district = districtRepository.findByDistrictName(districtName);
            if (district == null) {
                notFoundDistricts.add(districtName);
                continue;
            }
            if (alreadySubscribe(subscriptionDto.getEmail(), district)) {
                duplicatedDistricts.add(districtName);
            }
        }
    }

    private void processSubscriptions(SubscriptionDTO subscriptionDto, List<String> errorMessages) {
        for (String districtName : subscriptionDto.getDistrict()) {
            try {
                District district = districtRepository.findByDistrictName(districtName);
                createSubscription(subscriptionDto.getEmail(), district);
            } catch (SubscriptionAppException e) {
                errorMessages.add(e.getMessage());
            }
        }
    }

    private boolean alreadySubscribe(String email,District district) {
        List<Subscription> subscriptions = subscriptionRepository.findByEmailAndDistrict(email, district);
        return !subscriptions.isEmpty();
    }

    private void createSubscription(String email, District district) {
        Subscription subscription = Subscription.builder()
            .email(email)
            .district(district)
            .build();
        subscriptionRepository.save(subscription);
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
