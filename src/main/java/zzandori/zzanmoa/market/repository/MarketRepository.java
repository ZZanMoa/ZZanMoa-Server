package zzandori.zzanmoa.market.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zzandori.zzanmoa.market.entity.Market;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Optional<Market> findByMarketIdAndItemId(String marketId, String itemId);

    @Query("SELECT DISTINCT m.marketName FROM Market m")
    List<String> findDistinctMarketName();

    @Query("SELECT m FROM Market m WHERE m.marketName IN :marketNames")
    List<Market> findMarketsByNames(@Param("marketNames") List<String> marketNames);

    @Query("SELECT m FROM Market m WHERE m.marketName IN :marketNames AND m.itemName IN :itemNames")
    List<Market> findMarketsByNamesAndItems(@Param("marketNames") List<String> marketNames, @Param("itemNames") List<String> itemNames);

    Market save(Market market);
}
