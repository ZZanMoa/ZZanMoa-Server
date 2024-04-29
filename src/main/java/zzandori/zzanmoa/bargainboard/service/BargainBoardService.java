package zzandori.zzanmoa.bargainboard.service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.bargainboard.dto.BargainResponseDTO;
import zzandori.zzanmoa.bargainboard.dto.DistrictResponseDTO;
import zzandori.zzanmoa.bargainboard.dto.PaginatedResponseDTO;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.entity.District;
import zzandori.zzanmoa.bargainboard.entity.Event;
import zzandori.zzanmoa.bargainboard.repository.BargainBoardRepository;
import zzandori.zzanmoa.bargainboard.repository.DistrictRepository;

@RequiredArgsConstructor
@Service
public class BargainBoardService {

    private final BargainBoardRepository bargainBoardRepository;
    private final DistrictRepository districtRepository;

    public List<DistrictResponseDTO> getDistrict(){
        List<District> districtList = districtRepository.findAll();
        return mapDistrictsToDTOs(districtList);
    }

    public PaginatedResponseDTO<BargainResponseDTO> getBargainBoard(String id, int page) {
        Page<BargainBoard> bargainBoards = fetchBargainBoards(id, page);
        int recentNewsCount = getRecentNewsCount();
        return buildPaginatedResponse(bargainBoards, recentNewsCount);
    }


    private Page<BargainBoard> fetchBargainBoards(String id, int page) {
        Pageable pageable = PageRequest.of(page, 9);
        if (id == null) {
            return bargainBoardRepository.findAll(pageable);
        } else {
            Event event = mapIdToEvent(id);
            return bargainBoardRepository.findByEvent(event, pageable);
        }
    }

    private Event mapIdToEvent(String id) {
        if ("1".equals(id)) {
            return Event.DISCOUNT_SALE;
        } else if ("2".equals(id)) {
            return Event.DIRECT_TRADE;
        }
        return null;
    }

    private int getRecentNewsCount() {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        return bargainBoardRepository.countRecentNews(oneWeekAgo);
    }

    private List<DistrictResponseDTO> mapDistrictsToDTOs(List<District> districts) {
        return districts.stream()
            .map(this::mapToDistrictResponseDTO)
            .collect(Collectors.toList());
    }

    private PaginatedResponseDTO<BargainResponseDTO> buildPaginatedResponse(Page<BargainBoard> page, int recentNewsCount) {
        Page<BargainResponseDTO> responsePage = page.map(this::mapToBargainResponseDTO);
        return new PaginatedResponseDTO<>(responsePage, recentNewsCount);
    }

    private DistrictResponseDTO mapToDistrictResponseDTO(District district) {
        return DistrictResponseDTO.builder()
            .districtId(district.getId())
            .districtName(district.getDistrictName())
            .build();
    }

    private BargainResponseDTO mapToBargainResponseDTO(BargainBoard bargainBoard) {
        return BargainResponseDTO.builder()
            .id(bargainBoard.getId())
            .eventId(bargainBoard.getEvent().getId())
            .districtId(bargainBoard.getDistrict().getId())
            .title(bargainBoard.getTitle())
            .content(bargainBoard.getContent())
            .createdAt(bargainBoard.getCreatedAt())
            .build();
    }
}
