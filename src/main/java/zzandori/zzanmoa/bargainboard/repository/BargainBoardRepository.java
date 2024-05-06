package zzandori.zzanmoa.bargainboard.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;

public interface BargainBoardRepository extends JpaRepository<BargainBoard, Long> {
    Page<BargainBoard> findByEventId(Integer eventId, Pageable pageable);
    Page<BargainBoard> findByEventIdAndDistrictId(Integer eventId, Integer districtId, Pageable pageable);

    @Query("SELECT b FROM BargainBoard b WHERE " +
        "(b.event.id = :eventId AND (b.district.id = :districtId OR :districtId IS NULL) AND " +
        "(b.title LIKE %:keyword% OR b.content LIKE %:keyword%))")
    Page<BargainBoard> findByEventIdAndDistrictIdAndKeyword(Integer eventId, Integer districtId, String keyword, Pageable pageable);

    @Query("SELECT count(b) FROM BargainBoard b WHERE b.createdAt >= :startDate")
    int countRecentNews(LocalDate startDate);

    List<BargainBoard> findByCreatedAtAfter(LocalDate dateTime);

}
