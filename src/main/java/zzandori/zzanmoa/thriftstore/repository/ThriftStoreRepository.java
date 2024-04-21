package zzandori.zzanmoa.thriftstore.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zzandori.zzanmoa.thriftstore.dto.CategoryPriceDTO;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;

public interface ThriftStoreRepository extends JpaRepository<ThriftStore, Long> {

    Optional<ThriftStore> findByStoreIdAndItemId(String storeId, String itemId);


    @Query("SELECT new zzandori.zzanmoa.thriftstore.dto.CategoryPriceDTO(t.category, MIN(t.price), MAX(t.price)) " +
        "FROM ThriftStore t WHERE t.price > 0 " +
        "GROUP BY t.category")
    List<CategoryPriceDTO> findCategoryPrices();

}
