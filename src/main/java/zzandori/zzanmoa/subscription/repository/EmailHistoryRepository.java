package zzandori.zzanmoa.subscription.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.subscription.entity.EmailHistory;
import zzandori.zzanmoa.subscription.entity.Subscription;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long> {

    boolean existsBySubscriptionAndBargainAndStatus(Subscription subscription, BargainBoard bargainBoard, String status);
    List<EmailHistory> findBySubscription(Subscription subscription);

}
