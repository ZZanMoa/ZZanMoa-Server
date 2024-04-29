package zzandori.zzanmoa.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.subscription.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {


}
