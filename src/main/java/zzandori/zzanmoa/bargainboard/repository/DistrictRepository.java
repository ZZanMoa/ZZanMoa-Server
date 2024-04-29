package zzandori.zzanmoa.bargainboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.bargainboard.entity.District;

public interface DistrictRepository extends JpaRepository<District, Long> {
    District findByDistrictId(Integer districtId);

    boolean existsByDistrictId(Integer districtId);

}
