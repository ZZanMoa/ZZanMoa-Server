package zzandori.zzanmoa.item.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zzandori.zzanmoa.item.entity.Item;
import zzandori.zzanmoa.market.entity.Market;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.itemName IN :itemNames")
    List<Item> findByItemNames(@Param("itemNames") List<String> itemNames);

    Optional<Item> findByItemName(String itemName);
}
