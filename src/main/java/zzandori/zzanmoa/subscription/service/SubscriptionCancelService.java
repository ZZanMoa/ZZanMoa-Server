package zzandori.zzanmoa.subscription.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.common.dto.SuccessResponseDTO;
import zzandori.zzanmoa.exception.subscription.SubscriptionAppException;
import zzandori.zzanmoa.exception.subscription.SubscriptionErrorCode;
import zzandori.zzanmoa.subscription.dto.SubscriptionCancelDTO;
import zzandori.zzanmoa.subscription.entity.EmailHistory;
import zzandori.zzanmoa.subscription.entity.Subscription;
import zzandori.zzanmoa.subscription.repository.EmailHistoryRepository;
import zzandori.zzanmoa.subscription.repository.SubscriptionRepository;

@Service
@RequiredArgsConstructor
public class SubscriptionCancelService {

    private final SubscriptionRepository subscriptionRepository;
    private final EmailHistoryRepository emailHistoryRepository;

    @Transactional
    public ResponseEntity<?> cancelSubscription(SubscriptionCancelDTO subscriptionCancelDTO){
        List<Subscription> subscriptionList = findSubscriptions(subscriptionCancelDTO);

        if (subscriptionList.isEmpty()) {
            throw new SubscriptionAppException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND);
        }

        setNullSubscriptionInEmailHistory(subscriptionList);
        deleteSubscriptions(subscriptionList);

        return ResponseEntity.ok().body(SuccessResponseDTO.builder()
            .statusCode(HttpStatus.OK.value())
            .message("구독 취소하기 성공")
            .build());
    }

    private List<Subscription> findSubscriptions(SubscriptionCancelDTO subscriptionCancelDTO){
        return subscriptionRepository.findByEmailAndName(subscriptionCancelDTO.getEmail(),
            subscriptionCancelDTO.getName());
    }

    private void deleteSubscriptions(List<Subscription> subscriptions) {
        subscriptionRepository.deleteAll(subscriptions);
    }

    private void setNullSubscriptionInEmailHistory(List<Subscription> subscriptions) {
        subscriptions.forEach(subscription -> {
            List<EmailHistory> emailHistories = emailHistoryRepository.findBySubscription(subscription);
            emailHistories.forEach(emailHistory -> {
                emailHistory.setSubscription(null);
                emailHistoryRepository.save(emailHistory);
            });
        });
    }
}
