package zzandori.zzanmoa.bargainboard.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.bargainboard.service.BargainBoardService;
import zzandori.zzanmoa.bargainboard.service.DataSettingService;

@Tag(name = "BargainBoardController", description = "서울시 할인행사 & 직거래 마켓 정보 알리미 컨트롤러")

@RestController
@RequiredArgsConstructor
@RequestMapping("/bargain-board")
public class BargainBoardController {

    private final BargainBoardService bargainBoardService;
    private final DataSettingService dataSettingService;


    @GetMapping("/save/district")
    public void setBargainDistrict(){
        dataSettingService.saveBargainDistrictData();
    }

    @GetMapping("/save/bargain")
    public void setBargainBoard(){
        dataSettingService.saveBargainBoardData();
    }



}
