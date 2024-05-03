package zzandori.zzanmoa.bargainboard.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.bargainboard.dto.BargainResponseDTO;
import zzandori.zzanmoa.bargainboard.dto.DistrictResponseDTO;
import zzandori.zzanmoa.bargainboard.dto.PaginatedResponseDTO;
import zzandori.zzanmoa.bargainboard.service.BargainBoardService;
import zzandori.zzanmoa.bargainboard.service.DataSettingService;

@Tag(name = "BargainBoardController", description = "서울시 할인행사 & 직거래 마켓 정보 알리미 컨트롤러")

@RestController
@RequiredArgsConstructor
@RequestMapping("/bargain-board")
public class BargainBoardController {

    private final BargainBoardService bargainBoardService;
    private final DataSettingService dataSettingService;

    @GetMapping("/")
    public PaginatedResponseDTO<BargainResponseDTO> getBargainBoard(@RequestParam(required = false) String id, @RequestParam(defaultValue = "0") int page) {
        return bargainBoardService.getBargainBoard(id, page);
    }

    @GetMapping("/get/district")
    public List<DistrictResponseDTO> getDistrict(){
        return bargainBoardService.getDistrict();
    }

    @GetMapping("/save/district")
    public void setBargainDistrict(){
        dataSettingService.saveBargainDistrictData();
    }

    @GetMapping("/save/bargain")
    public void setBargain(){
        dataSettingService.saveBargainData();
    }

    @GetMapping("/save/living-cost")
    public void setLivingCost(){
        dataSettingService.saveLivingCostData();
    }



}
