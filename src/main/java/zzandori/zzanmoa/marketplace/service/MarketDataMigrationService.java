package zzandori.zzanmoa.marketplace.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.dto.Candidate;
import zzandori.zzanmoa.googleapi.dto.Location;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.repository.MarketRepository;
import zzandori.zzanmoa.marketplace.entity.MarketPlace;
import zzandori.zzanmoa.marketplace.repository.MarketPlaceRepository;

@RequiredArgsConstructor
@Service
public class MarketDataMigrationService {


    private final MarketRepository marketRepository;
    private final MarketPlaceRepository marketPlaceRepository;
    private final GoogleMapApiService googleMapApiService;


    public void saveMarketPlaces() {
        List<Market> markets = marketRepository.findAll();
        markets.stream().collect(Collectors.toMap(Market::getMarketId, Function.identity(),
            (existing, replacement) -> existing)).values().forEach(market -> {
            try {
                saveMarketPlace(market);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveMarketPlace(Market market) throws UnsupportedEncodingException {
        Candidate candidate = googleMapApiService.requestFindPlace(market.getMarketName());
        String formattedAddress = candidate != null ? candidate.getFormatted_address() : null;

        Location location = getLocationFromAddress(formattedAddress);

        MarketPlace marketPlace = buildMarketPlace(market, formattedAddress, location);
        marketPlaceRepository.save(marketPlace);
    }

    private Location getLocationFromAddress(String address) throws UnsupportedEncodingException {
        return address != null ? googleMapApiService.requestGeocode(address) : null;
    }

    private MarketPlace buildMarketPlace(Market market, String formattedAddress, Location location) {
        return MarketPlace.builder()
            .marketId(market.getMarketId())
            .marketName(market.getMarketName())
            .marketAddress(formattedAddress)
            .latitude(location != null ? location.getLat() : null)
            .longitude(location != null ? location.getLng() : null)
            .build();
    }

}
