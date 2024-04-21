package zzandori.zzanmoa.marketplace.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.market.dto.MarketResponseDto;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.repository.MarketRepository;
import zzandori.zzanmoa.market.service.MarketService;
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

        Set<String> marketIds = markets.stream()
            .map(Market::getMarketId)
            .collect(Collectors.toSet());

        markets.stream()
            .filter(market -> marketIds.contains(market.getMarketId()))
            .forEach(this::saveMarketPlace);
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
                .build()) // 빌더를 사용하여 DTO 객체 생성
            .collect(Collectors.toList());
    }
}
