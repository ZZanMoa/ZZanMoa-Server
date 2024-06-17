package zzandori.zzanmoa.marketplace.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.dto.review.ReviewResponse;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.marketplace.dto.MarketPlaceResponseDto;
import zzandori.zzanmoa.marketplace.dto.MarketPlaceReviewResponseDto;
import zzandori.zzanmoa.marketplace.entity.MarketPlace;
import zzandori.zzanmoa.marketplace.repository.MarketPlaceGoogleIdsRepository;
import zzandori.zzanmoa.marketplace.repository.MarketPlaceRepository;

@RequiredArgsConstructor
@Service
public class MarketPlaceService {

    private final MarketPlaceRepository marketPlaceRepository;
    private final MarketPlaceGoogleIdsRepository marketPlaceGoogleIdsRepository;
    private final GoogleMapApiService googleMapApiService;


    public List<MarketPlaceResponseDto> getMarketPlaces() {
        List<MarketPlace> marketPlaces = marketPlaceRepository.findAll();
        return marketPlaces.stream()
            .filter(marketPlace -> marketPlace.getMarketAddress() != null)
            .map(
                marketPlace -> MarketPlaceResponseDto.builder()
                    .marketId(marketPlace.getMarketId())
                    .marketName(marketPlace.getMarketName())
                    .latitude(marketPlace.getLatitude())
                    .longitude(marketPlace.getLongitude())
                    .build())
            .collect(Collectors.toList());
    }

    public MarketPlaceReviewResponseDto buildMarketPlaceReviewResponse(String marketId) {
        return MarketPlaceReviewResponseDto.builder()
            .reviews(getReviews(marketId))
            .build();
    }

    public List<String> getReviews(String marketId){
        System.out.println("enter");
        return marketPlaceGoogleIdsRepository.findByMarketId(marketId)
            .map(placeId -> {
                ReviewResponse reviewResponse = googleMapApiService.requestReview(placeId);
                if (reviewResponse.getResult() == null || reviewResponse.getResult().getReviews() == null) {
                    System.out.println("null");
                    return null;
                }
                List<String> reviews = reviewResponse.getResult().getReviews().stream()
                    .map(review -> review.getText())
                    .collect(Collectors.toList());
                System.out.println(
                    "Arrays.toString(reviews.toArray()) = " + Arrays.toString(reviews.toArray()));
                return reviews;
            })
            .orElse(null);
    }


}
