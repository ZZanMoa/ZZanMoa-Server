package zzandori.zzanmoa.savingplace.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zzandori.zzanmoa.savingplace.entity.SavingStoreGoogleIds;

public interface SavingStoreGoogleIdsRepository extends JpaRepository<SavingStoreGoogleIds, Long> {
    @Query("SELECT s.placeId FROM SavingStoreGoogleIds s WHERE s.savingStore.storeId = :storeId")
    Optional<String> findByStoreId(String storeId);
}
