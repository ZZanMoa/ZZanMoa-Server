package zzandori.zzanmoa.savingplace.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.UnsupportedEncodingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.chatgptapi.service.ChatGPTService;
import zzandori.zzanmoa.common.dto.ApiResponse;
import zzandori.zzanmoa.savingplace.dto.SavingStoreReviewResponseDto;
import zzandori.zzanmoa.savingplace.service.SavingDataMigrationService;
import zzandori.zzanmoa.savingplace.service.SavingPlaceService;
import zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO;
import zzandori.zzanmoa.savingplace.dto.StoreInfoDTO;

@Tag(name = "SavingPlaceController", description = "알뜰가게 정보 응답 컨트롤러")

@RestController
@RequiredArgsConstructor
@RequestMapping("/saving-place")
public class SavingPlaceController {

    private final SavingPlaceService savingPlaceService;
    private final SavingDataMigrationService savingDataMigrationService;
    private final ChatGPTService chatGPTService;

    @GetMapping("/save")
    public void save() throws InterruptedException, UnsupportedEncodingException {
        savingDataMigrationService.save();
    }

    @GetMapping("/get/category")
    public List<CategoryPriceDTO> getCategoryPrice(){
        return savingPlaceService.getCategoryPrice();
    }

    @GetMapping("/get/store")
    public List<StoreInfoDTO> getStores(){
        return savingPlaceService.getAllStoresWithItems();
    }

    @GetMapping("/review/{storeId}")
    public ResponseEntity<ApiResponse<?>> getReviews(@PathVariable String storeId) {
        SavingStoreReviewResponseDto savingStoreReviewResponseDto = savingPlaceService.buildSavingStoreReviewResponse(storeId);
        List<String> reviews = savingStoreReviewResponseDto.getReviews();

        if (reviews == null) {
            return ResponseEntity.ok(ApiResponse.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("리뷰가 존재하지 않습니다.")
                .build());
        }

        String analyzedReview = chatGPTService.getChatResponse(reviews);
        return ResponseEntity.ok(ApiResponse.builder()
            .statusCode(HttpStatus.OK.value())
            .message("리뷰를 불러오는 데 성공하였습니다.")
            .data(analyzedReview)
            .build());
    }

}
