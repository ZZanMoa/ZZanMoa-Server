package zzandori.zzanmoa.subscription.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public ResponseEntity<?> cancelSubscription(SubscriptionCancelDTO subscriptionCancelDTO){
        String email = subscriptionCancelDTO.getEmail();;
        String name = subscriptionCancelDTO.getName();
        List<Subscription> subscriptionList = subscriptionRepository.findByEmailAndName(email, name);

        if (subscriptionList.isEmpty()) {
            return ResponseEntity.badRequest().body("해당하는 구독 정보를 찾을 수 없습니다.");
        }

        setNullSubscriptionInEmailHistory(subscriptionList);

        subscriptionRepository.deleteAll(subscriptionList);
        return ResponseEntity.ok().body("구독이 성공적으로 취소되었습니다.");
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
