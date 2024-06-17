package zzandori.zzanmoa.marketplace.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.dto.findplace.Candidate;
import zzandori.zzanmoa.googleapi.dto.findplace.FindPlaceResponse;
import zzandori.zzanmoa.googleapi.dto.geometry.GeocodeResponse;
import zzandori.zzanmoa.googleapi.dto.geometry.Geometry;
import zzandori.zzanmoa.googleapi.dto.geometry.Location;
import zzandori.zzanmoa.googleapi.dto.geometry.Result;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.repository.MarketRepository;
import zzandori.zzanmoa.marketplace.entity.MarketPlace;
import zzandori.zzanmoa.marketplace.entity.MarketPlaceGoogleIds;
import zzandori.zzanmoa.marketplace.repository.MarketPlaceGoogleIdsRepository;
import zzandori.zzanmoa.marketplace.repository.MarketPlaceRepository;

@RequiredArgsConstructor
@Service
public class MarketDataMigrationService {


    private final MarketRepository marketRepository;
    private final MarketPlaceRepository marketPlaceRepository;
    private final MarketPlaceGoogleIdsRepository marketPlaceGoogleIdsRepository;
    private final GoogleMapApiService googleMapApiService;


    public void saveMarketPlaces() {
        List<Market> markets = marketRepository.findAll();
        markets.stream().collect(Collectors.toMap(Market::getMarketId, Function.identity(),
            (existing, replacement) -> existing)).values().forEach(market -> {
            saveMarketPlace(market);
        });
    }

    public void saveMarketPlace(Market market) {
        FindPlaceResponse findPlaceResponse = googleMapApiService.requestFindPlace(
            market.getMarketName());
        List<Candidate> candidates = findPlaceResponse.getCandidates();

        String formattedAddress = null;
        List<Result> results = null;
        if(candidates.size() > 0) {
            Candidate candidate = findPlaceResponse.getCandidates().get(0);
            formattedAddress = candidate.getFormatted_address();
            GeocodeResponse geocodeResponse = googleMapApiService.requestGeocode(formattedAddress);
            results = geocodeResponse.getResults();
        }


        Location location = results != null ? results.get(0).getGeometry().getLocation() : null;
        String placeId = results != null ? results.get(0).getPlace_id() : null;

        MarketPlace marketPlace = buildMarketPlace(market, formattedAddress, location);
        marketPlaceRepository.save(marketPlace);

        MarketPlaceGoogleIds marketPlaceGoogleIds = buildMarketPlaceGoogleIds(placeId, marketPlace);
        marketPlaceGoogleIdsRepository.save(marketPlaceGoogleIds);

    }

    private MarketPlace buildMarketPlace(Market market, String formattedAddress,
        Location location) {
        return MarketPlace.builder()
            .marketId(market.getMarketId())
            .marketName(market.getMarketName())
            .marketAddress(formattedAddress)
            .latitude(location != null ? location.getLat() : null)
            .longitude(location != null ? location.getLng() : null)
            .build();
    }

    private MarketPlaceGoogleIds buildMarketPlaceGoogleIds(String placeId,
        MarketPlace marketPlace) {
        return MarketPlaceGoogleIds.builder()
            .placeId(placeId)
            .marketPlace(marketPlace)
            .build();
    }

}
