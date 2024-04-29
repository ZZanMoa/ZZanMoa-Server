package zzandori.zzanmoa.bargainboard.service;


import java.util.ArrayList;
import java.util.List;
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


    public PaginatedResponseDTO<BargainResponseDTO> getBargainBoard(String id, int page) {
        Pageable pageable = PageRequest.of(page, 9); // Fixed page size of 9
        Page<BargainBoard> bargainBoards;

        if (id == null) {
            bargainBoards = bargainBoardRepository.findAll(pageable);
        } else {
            Event event = null;
            if ("1".equals(id)) {
                event = Event.DISCOUNT_SALE;
            } else if ("2".equals(id)) {
                event = Event.DIRECT_TRADE;
            }
            bargainBoards = bargainBoardRepository.findByEvent(event, pageable);
        }

        Page<BargainResponseDTO> responsePage = bargainBoards.map(this::mapToBargainResponseDTO);
        return new PaginatedResponseDTO<>(responsePage);
    }


    private DistrictResponseDTO mapToDistrictResponseDTO(District district){
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
