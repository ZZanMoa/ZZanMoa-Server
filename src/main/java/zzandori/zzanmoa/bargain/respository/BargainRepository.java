package zzandori.zzanmoa.bargain.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import zzandori.zzanmoa.bargain.entity.Bargain;

public interface BargainRepository extends JpaRepository<Bargain, Long> {

}
