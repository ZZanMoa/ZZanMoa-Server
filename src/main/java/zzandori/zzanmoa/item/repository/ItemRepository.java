package zzandori.zzanmoa.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
