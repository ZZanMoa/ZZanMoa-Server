package zzandori.zzanmoa.thriftstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zzandori.zzanmoa.thriftstore.entity.SavingStore;

public interface SavingStoreRepository extends JpaRepository<SavingStore, Long> {

    @Query("SELECT s FROM SavingStore s JOIN FETCH s.items")
    List<SavingStore> findAllStoreWithItems();
}
