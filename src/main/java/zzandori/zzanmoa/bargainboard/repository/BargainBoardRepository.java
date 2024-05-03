package zzandori.zzanmoa.bargainboard.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.entity.District;
import zzandori.zzanmoa.bargainboard.entity.Event;

public interface BargainBoardRepository extends JpaRepository<BargainBoard, Long> {
    Page<BargainBoard> findByEvent(Event event, Pageable pageable);
    Page<BargainBoard> findByDistrictId(Integer districtId, Pageable pageable);
    Page<BargainBoard> findByEventAndDistrictId(Event event, Integer districtId, Pageable pageable);

    Page<BargainBoard> findAll(Pageable pageable);

    @Query("SELECT count(b) FROM BargainBoard b WHERE b.createdAt >= :startDate")
    int countRecentNews(LocalDate startDate);

    List<BargainBoard> findByCreatedAtAfter(LocalDate dateTime);

}
