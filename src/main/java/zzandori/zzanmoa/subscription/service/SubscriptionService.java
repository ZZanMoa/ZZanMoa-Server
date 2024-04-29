package zzandori.zzanmoa.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzandori.zzanmoa.bargainboard.entity.District;
import zzandori.zzanmoa.bargainboard.repository.DistrictRepository;
import zzandori.zzanmoa.subscription.dto.SubscriptionDTO;
import zzandori.zzanmoa.subscription.dto.SubscriptionStatusDTO;
import zzandori.zzanmoa.subscription.entity.Subscription;
import zzandori.zzanmoa.subscription.repository.SubscriptionRepository;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final DistrictRepository districtRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public SubscriptionStatusDTO subscribe(SubscriptionDTO subscriptionDto) {
        boolean allSavedSuccessfully = true;

        for (String districtName : subscriptionDto.getDistrict()) {
            try {
                District district = districtRepository.findByDistrictName(districtName);
                Subscription subscription = Subscription.builder()
                    .email(subscriptionDto.getEmail())
                    .district(district)
                    .build();
                subscriptionRepository.save(subscription);

            } catch (Exception e) {
                allSavedSuccessfully = false;
            }
        }

        return SubscriptionStatusDTO.builder()
            .status(allSavedSuccessfully)
            .build();
    }

}
