package zzandori.zzanmoa.bargainboard.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.bargain.entity.Bargain;
import zzandori.zzanmoa.bargain.respository.BargainRepository;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.entity.District;
import zzandori.zzanmoa.bargainboard.entity.Event;
import zzandori.zzanmoa.bargainboard.repository.BargainBoardRepository;
import zzandori.zzanmoa.bargainboard.repository.DistrictRepository;

@RequiredArgsConstructor
@Service
public class DataSettingService {
    private final BargainRepository bargainRepository;
    private final BargainBoardRepository bargainBoardRepository;
    private final DistrictRepository districtRepository;

    public void saveBargainBoardData() {
        List<Bargain> bargains = bargainRepository.findAll();

        for (Bargain bargain : bargains) {
            District district = districtRepository.findByDistrictId(bargain.getDistrictId());
            Event event = Event.getById(bargain.getEventId());

            BargainBoard bargainBoard = BargainBoard.builder()
                .district(district)
                .event(event)
                .title(bargain.getTitle())
                .content(bargain.getContent())
                .views(bargain.getViews())
                .createdAt(bargain.getCreatedAt().toLocalDateTime().toLocalDate())
                .build();

            bargainBoardRepository.save(bargainBoard);
        }
    }


    public void saveBargainDistrictData(){
        List<Bargain> bargains = bargainRepository.findAll();
        Set<Integer> processedDistrictIds = new HashSet<>();

        for (Bargain bargain : bargains) {
            if (!processedDistrictIds.contains(bargain.getDistrictId()) &&
                !districtRepository.existsByDistrictId(bargain.getDistrictId())) {

                District district = District.builder()
                    .districtId(bargain.getDistrictId())
                    .districtName(bargain.getDistrictName())
                    .build();

                districtRepository.save(district);
                processedDistrictIds.add(bargain.getDistrictId());
            }
        }
    }

}
