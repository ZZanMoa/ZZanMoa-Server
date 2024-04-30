package zzandori.zzanmoa.subscription.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.entity.District;
import zzandori.zzanmoa.bargainboard.repository.DistrictRepository;
import zzandori.zzanmoa.subscription.dto.SubscriptionDTO;
import zzandori.zzanmoa.subscription.dto.SubscriptionStatusDTO;
import zzandori.zzanmoa.subscription.entity.EmailHistory;
import zzandori.zzanmoa.subscription.entity.Subscription;
import zzandori.zzanmoa.subscription.repository.EmailHistoryRepository;
import zzandori.zzanmoa.subscription.repository.SubscriptionRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    @Autowired
    private JavaMailSender emailSender;

    private final DistrictRepository districtRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailHistoryRepository emailHistoryRepository;

    @Transactional
    public SubscriptionStatusDTO subscribe(SubscriptionDTO subscriptionDto) {
        boolean allSavedSuccessfully = true;

        for (String districtName : subscriptionDto.getDistrict()) {
            if (!createSubscription(districtName, subscriptionDto.getEmail())) {
                allSavedSuccessfully = false;
            }
        }

        return SubscriptionStatusDTO.builder()
            .status(allSavedSuccessfully)
            .build();
    }

    private boolean createSubscription(String districtName, String email) {
        try {
            District district = districtRepository.findByDistrictName(districtName);
            Subscription subscription = Subscription.builder()
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
            return; // 중복 이메일 방지
        }
        prepareAndSendEmail(to, subject, text, subscription, bargainBoard);
    }

    private void prepareAndSendEmail(String to, String subject, String text, Subscription subscription, BargainBoard bargainBoard) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("zzanmoa@gmail.com");
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
