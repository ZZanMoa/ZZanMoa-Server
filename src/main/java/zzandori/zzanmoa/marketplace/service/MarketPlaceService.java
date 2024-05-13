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

    private final MarketPlaceRepository marketPlaceRepository;


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

}
