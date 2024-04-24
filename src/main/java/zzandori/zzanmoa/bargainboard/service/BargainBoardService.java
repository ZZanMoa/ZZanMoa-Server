package zzandori.zzanmoa.bargainboard.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.bargainboard.dto.BargainResponseDTO;
import zzandori.zzanmoa.bargainboard.entity.BargainBoard;
import zzandori.zzanmoa.bargainboard.repository.BargainBoardRepository;

@RequiredArgsConstructor
@Service
public class BargainBoardService {

    private final BargainBoardRepository bargainBoardRepository;

    public List<BargainResponseDTO> getTotalBargainBoard() {
        List<BargainBoard> bargainBoards = bargainBoardRepository.findAll();
        List<BargainResponseDTO> responseDTOs = new ArrayList<>();

        for (BargainBoard bargainBoard : bargainBoards) {
            BargainResponseDTO responseDTO = mapToBargainResponseDTO(bargainBoard);
            responseDTOs.add(responseDTO);
        }

        return responseDTOs;
    }


    private BargainResponseDTO mapToBargainResponseDTO(BargainBoard bargainBoard) {
        return BargainResponseDTO.builder()
            .id(bargainBoard.getId())
            .districtName(bargainBoard.getDistrict().getDistrictName())
            .title(bargainBoard.getTitle())
            .createdAt(bargainBoard.getCreatedAt())
            .build();
    }
}
