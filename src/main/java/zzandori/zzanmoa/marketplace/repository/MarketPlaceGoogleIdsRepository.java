package zzandori.zzanmoa.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zzandori.zzanmoa.marketplace.entity.MarketPlaceGoogleIds;

@Repository
public interface MarketPlaceGoogleIdsRepository extends JpaRepository<MarketPlaceGoogleIds, Long> {

}
