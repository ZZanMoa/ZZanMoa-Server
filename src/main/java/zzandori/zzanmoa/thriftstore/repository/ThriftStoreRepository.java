package zzandori.zzanmoa.thriftstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;

public interface ThriftStoreRepository extends JpaRepository<ThriftStore, Long> {

    Optional<ThriftStore> findByStoreIdAndItemId(String storeId, String itemId);


}
