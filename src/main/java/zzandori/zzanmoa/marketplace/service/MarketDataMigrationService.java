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
            saveMarketPlaceAndGoogleIds(market);
        });
    }

    public void saveMarketPlaceAndGoogleIds(Market market) {
        FindPlaceResponse findPlaceResponse = googleMapApiService.requestFindPlace(market.getMarketName());
        List<Candidate> candidates = findPlaceResponse.getCandidates();

        if (!candidates.isEmpty()) {
            Candidate candidate = candidates.get(0);
            String formattedAddress = candidates.get(0).getFormatted_address();
            String placeId = candidate.getPlace_id();
            Location location = fetchLocation(formattedAddress);

            MarketPlace marketPlace = buildMarketPlace(market, formattedAddress, location);
            marketPlaceRepository.save(marketPlace);

            MarketPlaceGoogleIds marketPlaceGoogleIds = buildMarketPlaceGoogleIds(placeId, marketPlace);
            marketPlaceGoogleIdsRepository.save(marketPlaceGoogleIds);
        }
    }

    private Location fetchLocation(String address) {
        GeocodeResponse geocodeResponse = googleMapApiService.requestGeocode(address);
        return Optional.ofNullable(geocodeResponse)
            .map(GeocodeResponse::getResults)
            .filter(results -> !results.isEmpty())
            .map(results -> results.get(0).getGeometry().getLocation()).orElse(null);
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
