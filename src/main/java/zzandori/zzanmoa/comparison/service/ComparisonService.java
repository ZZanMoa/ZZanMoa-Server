package zzandori.zzanmoa.comparison.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.comparison.dto.ComparisonRequestDto;
import zzandori.zzanmoa.comparison.dto.ComparisonResponseDto;
import zzandori.zzanmoa.comparison.dto.ItemDto;
import zzandori.zzanmoa.comparison.dto.MarketItemDto;
import zzandori.zzanmoa.comparison.dto.MarketPlaceDto;
import zzandori.zzanmoa.comparison.dto.RankDto;
import zzandori.zzanmoa.comparison.dto.SavingDto;
import zzandori.zzanmoa.item.entity.Item;
import zzandori.zzanmoa.item.service.ItemService;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.service.MarketService;

@RequiredArgsConstructor
@Service
public class ComparisonService {

    private final ItemService itemService;
    private final MarketService marketService;

    public ComparisonResponseDto comparePrice(ComparisonRequestDto request) {
        List<ItemDto> itemDtos = getItems(request);
        List<RankDto> rankedMarketItems = compareMarketItems(request);
        return ComparisonResponseDto.builder()
            .itemList(itemDtos)
            .rankList(rankedMarketItems)
            .build();
    }

    public List<ItemDto> getItems(ComparisonRequestDto request) {
        return itemService.getItemsByItemNames(request.getItemNames()).stream()
            .map(item -> ItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .average_price(item.getAveragePrice())
                .build())
            .collect(Collectors.toList());
    }

    public List<RankDto> compareMarketItems(ComparisonRequestDto request) {
        Map<String, List<Market>> marketGroups = loadMarketData(request);
        List<RankDto> sortedRankDtos = createAndSortRankDtos(marketGroups, request);
        return assignRanks(sortedRankDtos);
    }

    private Map<String, List<Market>> loadMarketData(ComparisonRequestDto request) {
        List<Item> items = itemService.getItemsByItemNames(request.getItemNames());
        List<Market> markets = marketService.getMarketsByNames(request.getMarketNames());
        return markets.stream()
            .collect(Collectors.groupingBy(Market::getMarketName));
    }

    private List<RankDto> createAndSortRankDtos(Map<String, List<Market>> marketGroups, ComparisonRequestDto request) {
        List<Item> items = itemService.getItemsByItemNames(request.getItemNames());
        return marketGroups.entrySet().stream()
            .map(entry -> buildRankDto(entry.getKey(), entry.getValue(), items))
            .sorted(Comparator.comparingInt(RankDto::getTotalSaving))
            .collect(Collectors.toList());
    }

    private List<RankDto> assignRanks(List<RankDto> rankDtos) {
        return IntStream.range(0, rankDtos.size())
            .mapToObj(index -> new RankDto(index + 1, rankDtos.get(index).getMarket(),
                rankDtos.get(index).getSavingList(), rankDtos.get(index).getTotalSaving()))
            .collect(Collectors.toList());
    }

    private RankDto buildRankDto(String marketName, List<Market> markets, List<Item> items) {
        MarketPlaceDto marketDto = new MarketPlaceDto(markets.get(0).getMarketId(), marketName);
        List<SavingDto> savings = items.stream()
            .map(item -> createSavingDto(findMarketForItem(markets, item), item))
            .collect(Collectors.toList());
        int totalSaving = savings.stream().mapToInt(SavingDto::getSaving).sum();
        return new RankDto(0, marketDto, savings, totalSaving);
    }

    private Market findMarketForItem(List<Market> markets, Item item) {
        return markets.stream()
            .filter(market -> market.getItemName().equals(item.getItemName()))
            .findFirst()
            .orElse(null);
    }

    private SavingDto createSavingDto(Market market, Item item) {
        int saving = (market != null) ? item.getAveragePrice() - (market != null ? market.getPrice() : 0): 0;
        return new SavingDto(new MarketItemDto(market != null ? market.getItemId() : item.getItemId(), item.getItemName(),
            market != null ? market.getPrice() : 0, market != null && market.getPrice() < item.getAveragePrice()), saving);
    }
}
