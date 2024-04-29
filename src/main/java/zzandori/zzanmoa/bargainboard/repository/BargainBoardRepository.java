package zzandori.zzanmoa.bargainboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.entity.Event;

public interface BargainBoardRepository extends JpaRepository<BargainBoard, Long> {
    Page<BargainBoard> findByEvent(Event event, Pageable pageable);
    Page<BargainBoard> findAll(Pageable pageable);
}
