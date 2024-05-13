package zzandori.zzanmoa.bargainboard.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.bargainboard.dto.BargainResponseDTO;
import zzandori.zzanmoa.bargainboard.dto.DistrictResponseDTO;
import zzandori.zzanmoa.bargainboard.dto.StatusResponseDTO;
import zzandori.zzanmoa.bargainboard.dto.PaginatedResponseDTO;
import zzandori.zzanmoa.bargainboard.service.BargainBoardService;
import zzandori.zzanmoa.bargainboard.service.BargainDataMigrationService;

@Tag(name = "BargainBoardController", description = "서울시 할인행사 & 직거래 마켓 정보 알리미 컨트롤러")

@RestController
@RequiredArgsConstructor
@RequestMapping("/bargain-board")
public class BargainBoardController {

    private final BargainBoardService bargainBoardService;
    private final BargainDataMigrationService bargainDataMigrationService;

    @GetMapping("/")
    public ResponseEntity<?> getBargainBoard(@RequestParam(required = false) Integer eventId, @RequestParam(required = false) Integer districtId, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page) {
        if (eventId == null) {
            return ResponseEntity.badRequest().body(new StatusResponseDTO("이벤트 ID는 필수입니다."));
        }
        try {
            PaginatedResponseDTO<BargainResponseDTO> response = bargainBoardService.getBargainBoard(eventId, districtId, page, keyword);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(new StatusResponseDTO("조회된 게시글이 없습니다."));
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new StatusResponseDTO("할인 게시판 검색 중 오류 발생: " + e.getMessage()));
        }
    }


    @GetMapping("/get/district")
    public List<DistrictResponseDTO> getDistrict(){
        return bargainBoardService.getDistrict();
    }

    @GetMapping("/save/district")
    public void setBargainDistrict(){
        bargainDataMigrationService.saveBargainDistrictData();
    }

    @GetMapping("/save/bargain")
    public void setBargain(){
        bargainDataMigrationService.saveBargainData();
    }

    @GetMapping("/save/living-cost")
    public void setLivingCost(){
        bargainDataMigrationService.saveLivingCostData();
    }



}
