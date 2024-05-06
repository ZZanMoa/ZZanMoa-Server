package zzandori.zzanmoa.bargainboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.bargainboard.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByEventId(Integer eventId);
}
