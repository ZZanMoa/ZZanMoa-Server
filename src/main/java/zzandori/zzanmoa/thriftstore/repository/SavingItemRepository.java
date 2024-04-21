package zzandori.zzanmoa.thriftstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zzandori.zzanmoa.thriftstore.dto.CategoryPriceDTO;
import zzandori.zzanmoa.thriftstore.entity.SavingItem;

public interface SavingItemRepository extends JpaRepository<SavingItem, Long> {

    @Query("SELECT new zzandori.zzanmoa.thriftstore.dto.CategoryPriceDTO(i.category, MIN(i.price), MAX(i.price)) " +
        "FROM SavingItem i WHERE i.price > 0 " +
        "GROUP BY i.category")
    List<CategoryPriceDTO> findCategoryPrices();

}
