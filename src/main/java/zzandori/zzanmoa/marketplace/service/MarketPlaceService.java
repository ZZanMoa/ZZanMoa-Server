package zzandori.zzanmoa.marketplace.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public void saveMarketPlaces() {
        List<Market> markets = marketRepository.findAll();

        markets.stream()
            .collect(Collectors.toMap(Market::getMarketId, Function.identity(),
                (existing, replacement) -> existing))
            .values()
            .forEach(market -> saveMarketPlace(market));
    }

    private void saveMarketPlace(Market market) {
        MarketPlace marketPlace = MarketPlace.builder()
            .marketId(market.getMarketId())
            .marketName(market.getMarketName())
            .build();
        marketPlaceRepository.save(marketPlace);
    }

    public List<MarketPlaceResponseDto> getMarketPlaces() {
        List<MarketPlace> marketPlaces = marketPlaceRepository.findAll();
        return marketPlaces.stream()
            .map(marketPlace -> MarketPlaceResponseDto.builder()
                .marketId(marketPlace.getMarketId())
                .marketName(marketPlace.getMarketName())
                .build())
            .collect(Collectors.toList());
    }
}
