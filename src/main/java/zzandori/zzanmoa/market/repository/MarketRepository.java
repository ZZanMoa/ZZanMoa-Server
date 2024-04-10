package zzandori.zzanmoa.market.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.market.entity.Market;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Optional<Market> findByMarketIdAndItemId(String marketId, String itemId);

    Market save(Market market);
}
