package zzandori.zzanmoa.savingplace.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO;
import zzandori.zzanmoa.savingplace.entity.SavingItem;

public interface SavingItemRepository extends JpaRepository<SavingItem, Long> {

    @Query("SELECT new zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO(i.category, MIN(i.price), MAX(i.price)) " +
        "FROM SavingItem i GROUP BY i.category")
    List<CategoryPriceDTO> findCategoryPrices();

}
