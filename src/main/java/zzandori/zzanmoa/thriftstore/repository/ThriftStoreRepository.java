package zzandori.zzanmoa.thriftstore.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;

public interface ThriftStoreRepository extends JpaRepository<ThriftStore, Long> {

    Page<ThriftStore> findAll(Pageable pageable);
    Optional<ThriftStore> findByStoreIdAndItemId(String storeId, String itemId);

}
