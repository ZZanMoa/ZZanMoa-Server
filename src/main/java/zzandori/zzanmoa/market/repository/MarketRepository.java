package zzandori.zzanmoa.market.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zzandori.zzanmoa.market.entity.Market;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Optional<Market> findByMarketIdAndItemId(String marketId, String itemId);

    @Query("SELECT DISTINCT m.marketName FROM Market m")
    List<String> findDistinctMarketName();

    Market save(Market market);
}
