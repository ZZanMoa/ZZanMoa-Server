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
import zzandori.zzanmoa.marketplace.dto.MarketPlaceResponseDto;
import zzandori.zzanmoa.marketplace.entity.MarketPlace;
import zzandori.zzanmoa.marketplace.repository.MarketPlaceRepository;

@RequiredArgsConstructor
@Service
public class MarketPlaceService {

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

        Location location = null;
        if (candidate != null) {
            location = googleMapApiService.requestGeocode(candidate.getFormatted_address());
        }
        MarketPlace marketPlace = MarketPlace.builder().marketId(market.getMarketId())
            .marketName(market.getMarketName())
            .marketAddress(candidate != null ? candidate.getFormatted_address() : null)
            .latitude(location != null ? location.getLat() : null)
            .longitude(location != null ? location.getLng() : null).build();
        marketPlaceRepository.save(marketPlace);
    }

    public List<MarketPlaceResponseDto> getMarketPlaces() {
        List<MarketPlace> marketPlaces = marketPlaceRepository.findAll();
        return marketPlaces.stream().map(
            marketPlace -> MarketPlaceResponseDto.builder().marketId(marketPlace.getMarketId())
                .marketName(marketPlace.getMarketName()).latitude(marketPlace.getLatitude())
                .longitude(marketPlace.getLongitude()).build()).collect(Collectors.toList());
    }
}
