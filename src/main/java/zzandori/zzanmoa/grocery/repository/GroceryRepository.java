package zzandori.zzanmoa.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.grocery.entity.Grocery;

public interface GroceryRepository extends JpaRepository<Grocery, Long> {

}
