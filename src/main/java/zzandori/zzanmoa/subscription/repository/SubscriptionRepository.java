package zzandori.zzanmoa.subscription.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.bargainboard.entity.District;
import zzandori.zzanmoa.subscription.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByDistrict(District district);
    List<Subscription> findByEmail(String email);
    List<Subscription> findByEmailAndDistrict(String email, District district);

}
