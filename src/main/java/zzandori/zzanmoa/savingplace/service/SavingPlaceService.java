package zzandori.zzanmoa.savingplace.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.dto.review.Result;
import zzandori.zzanmoa.googleapi.dto.review.Review;
import zzandori.zzanmoa.googleapi.dto.review.ReviewResponse;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO;
import zzandori.zzanmoa.savingplace.dto.ItemInfoDTO;
import zzandori.zzanmoa.savingplace.dto.SavingStoreReviewResponseDto;
import zzandori.zzanmoa.savingplace.dto.StoreInfoDTO;
import zzandori.zzanmoa.savingplace.repository.SavingItemRepository;
import zzandori.zzanmoa.savingplace.repository.SavingStoreGoogleIdsRepository;
import zzandori.zzanmoa.savingplace.repository.SavingStoreRepository;
import zzandori.zzanmoa.thriftstore.service.ThriftStoreService;

@RequiredArgsConstructor
@Service
public class SavingPlaceService {

    private final SavingStoreRepository savingStoreRepository;
    private final SavingItemRepository savingItemRepository;
    private final SavingStoreGoogleIdsRepository savingStoreGoogleIdsRepository;
    private final GoogleMapApiService googleMapApiService;

    private static final Logger logger = LoggerFactory.getLogger(ThriftStoreService.class);

    public List<CategoryPriceDTO> getCategoryPrice() {
        List<CategoryPriceDTO> results = savingItemRepository.findCategoryPrices();
        logger.info("Fetched category prices size: {}", results.size());
        return results;
    }

    public List<StoreInfoDTO> getAllStoresWithItems() {
        return savingStoreRepository.findAllStoreWithItems().stream()
            .map(store -> new StoreInfoDTO(
                store.getStoreId(),
                decodeHtmlEntities(store.getStoreName()),
                store.getPhoneNumber(),
                decodeHtmlEntities(store.getAddress()),
                store.getLatitude(),
                store.getLongitude(),
                store.getItems().stream()
                    .map(item -> new ItemInfoDTO(item.getItemId(), item.getItemName(),
                        item.getCategory(), item.getPrice()))
                    .collect(Collectors.toList())))
            .collect(Collectors.toList());
    }



    private String decodeHtmlEntities(String input) {
        String correctedInput = correctHtmlEntities(input);
        return StringEscapeUtils.unescapeHtml4(correctedInput);
    }

    private String correctHtmlEntities(String input) {
        return input.replaceAll("& #(\\d+)", "&#$1;");
    }

    public SavingStoreReviewResponseDto buildSavingStoreReviewResponse(String storeId) {
        return SavingStoreReviewResponseDto.builder()
            .reviews(getReviews(storeId))
            .build();
    }

    private List<String> getReviews(String storeId) {
        return savingStoreGoogleIdsRepository.findByStoreId(storeId)
            .map(placeId -> {
                ReviewResponse reviewResponse = googleMapApiService.requestReview(placeId);
                if (reviewResponse.getResult() == null || reviewResponse.getResult().getReviews() == null) {
                    return null;
                }
                List<String> reviews = reviewResponse.getResult().getReviews().stream()
                    .map(Review::getText)
                    .collect(Collectors.toList());
                return reviews;
            })
            .orElse(null);
    }

}
