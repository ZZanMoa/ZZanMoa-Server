package zzandori.zzanmoa.marketplace.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.marketplace.dto.MarketPlaceResponseDto;
import zzandori.zzanmoa.marketplace.service.MarketPlaceService;

@Tag(name = "MarketPlaceController", description = "시장의 가게 정보 응답 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/market/market-place")
public class MarketPlaceController {

    private final MarketPlaceService marketPlaceService;

    @GetMapping("/save")
    public void saveGroceries() {
        marketPlaceService.saveMarketPlaces();
    }

    @GetMapping("/get")
    public ResponseEntity<List<MarketPlaceResponseDto>> getMarketPlaces() {
        List<MarketPlaceResponseDto> marketPlaceResponseDtos = marketPlaceService.getMarketPlaces();
        return ResponseEntity.ok(marketPlaceResponseDtos);
    }

}
