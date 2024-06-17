package zzandori.zzanmoa.marketplace.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zzandori.zzanmoa.marketplace.entity.MarketPlaceGoogleIds;

@Repository
public interface MarketPlaceGoogleIdsRepository extends JpaRepository<MarketPlaceGoogleIds, Long> {
    @Query("SELECT m.placeId FROM MarketPlaceGoogleIds m WHERE m.marketPlace.marketId = :marketId")
    Optional<String> findByMarketId(String marketId);
}
