package zzandori.zzanmoa.marketplace.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.UnsupportedEncodingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.common.dto.ApiResponse;
import zzandori.zzanmoa.marketplace.dto.MarketPlaceResponseDto;
import zzandori.zzanmoa.marketplace.dto.MarketPlaceReviewResponseDto;
import zzandori.zzanmoa.marketplace.service.MarketDataMigrationService;
import zzandori.zzanmoa.marketplace.service.MarketPlaceService;

@Tag(name = "MarketPlaceController", description = "시장의 가게 정보 응답 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/market/market-place")
public class MarketPlaceController {

    private final MarketPlaceService marketPlaceService;
    private final MarketDataMigrationService marketDataMigrationService;

    @GetMapping("/save")
    public void saveGroceries() {
        marketDataMigrationService.saveMarketPlaces();
    }

    @GetMapping("/get")
    public ResponseEntity<List<MarketPlaceResponseDto>> getMarketPlaces() {
        List<MarketPlaceResponseDto> marketPlaceResponseDtos = marketPlaceService.getMarketPlaces();
        return ResponseEntity.ok(marketPlaceResponseDtos);
    }

    @GetMapping("/review/{marketId}")
    public ResponseEntity<ApiResponse<?>> getReviews(@PathVariable String marketId)
        throws UnsupportedEncodingException {
        MarketPlaceReviewResponseDto marketPlaceReviewResponseDto = marketPlaceService.getReviews(
            marketId);

        if (marketPlaceReviewResponseDto == null) {
            return ResponseEntity.ok(ApiResponse.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("리뷰가 존재하지 않습니다.")
                .build());
        }

        return ResponseEntity.ok(ApiResponse.builder()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .message("리뷰를 불러오는 데 성공하였습니다.")
            .data(marketPlaceReviewResponseDto)
            .build());
    }

}
