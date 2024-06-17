package zzandori.zzanmoa.marketplace.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.marketplace.dto.MarketPlaceResponseDto;
import zzandori.zzanmoa.marketplace.dto.MarketPlaceReviewResponseDto;
import zzandori.zzanmoa.marketplace.entity.MarketPlace;
import zzandori.zzanmoa.marketplace.entity.MarketPlaceGoogleIds;
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

    public MarketPlaceReviewResponseDto getReviews(String marketId)
        throws UnsupportedEncodingException {
        Optional<String> placeId = marketPlaceGoogleIdsRepository.findByMarketId(marketId);
        if (placeId.isPresent()) {
            List<String> reviews = googleMapApiService.requestReview(placeId.get()).stream().map(review -> review.getText())
                .toList();
            return MarketPlaceReviewResponseDto.builder()
                .reviews(reviews)
                .build();
        }

        return null;
    }

}
