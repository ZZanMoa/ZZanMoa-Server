package zzandori.zzanmoa.comparison.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
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
        return ComparisonResponseDto.builder()
            .itemList(getItems(request))
            .rankList(compareMarketItems(request))
            .build();
    }

    public List<ItemDto> getItems(ComparisonRequestDto request) {
        List<Item> items = itemService.getItemsByItemNames(request.getItemNames());

        List<ItemDto> itemDtos = new ArrayList<>();
        for(Item item: items) {
            itemDtos.add(ItemDto.builder()
                    .itemId(item.getItemId())
                    .itemName(item.getItemName())
                    .average_price(item.getAveragePrice())
                    .build());
        }

        return itemDtos;
    }

    public PriorityQueue<RankDto> compareMarketItems(ComparisonRequestDto request) {
        List<Item> items = itemService.getItemsByItemNames(request.getItemNames());
        List<Market> markets = marketService.getMarketsByNames(request.getMarketNames());
        Map<String, List<Market>> marketGroups = markets.stream()
            .collect(Collectors.groupingBy(Market::getMarketName));

        // PriorityQueue를 사용하여 자동으로 정렬
        PriorityQueue<RankDto> priorityQueue = new PriorityQueue<>(
            Comparator.comparingInt(RankDto::getTotalSaving)
        );

        marketGroups.forEach((marketName, marketList) -> {
            MarketPlaceDto marketDto = MarketPlaceDto.builder()
                .marketId(marketList.stream().findAny().map(Market::getMarketId).orElse(""))
                .marketName(marketName)
                .build();

            List<SavingDto> savings = items.stream()
                .map(item -> marketList.stream()
                    .filter(market -> market.getItemName().equals(item.getItemName()))
                    .map(market -> createSavingDto(market, item))
                    .findFirst()
                    .map(List::of)
                    .orElseGet(() -> List.of(createSavingDtoWithZeroPrice(item)))
                )
                .flatMap(List::stream)
                .collect(Collectors.toList());

            int totalSaving = savings.stream()
                .mapToInt(SavingDto::getSaving)
                .sum();

            priorityQueue.add(new RankDto(0, marketDto, savings, totalSaving));  // 순위는 나중에 할당
        });

        // 순위를 할당하기 위해 요소를 재배열
        int rank = 1;
        PriorityQueue<RankDto> rankedQueue = new PriorityQueue<>(Comparator.comparingInt(RankDto::getRank));
        while (!priorityQueue.isEmpty()) {
            RankDto current = priorityQueue.poll();
            rankedQueue.add(new RankDto(rank++, current.getMarket(), current.getSavingList(), current.getTotalSaving()));
        }

        return rankedQueue;
    }

    private SavingDto createSavingDto(Market market, Item item) {
        int saving = item.getAveragePrice() - market.getPrice();
        return SavingDto.builder()
            .marketItem(MarketItemDto.builder()
                .itemId(market.getItemId())
                .itemName(market.getItemName())
                .price(market.getPrice())
                .isSale(true)
                .build())
            .saving(saving)
            .build();
    }

    private SavingDto createSavingDtoWithZeroPrice(Item item) {
        return SavingDto.builder()
            .marketItem(MarketItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getAveragePrice())
                .isSale(false)
                .build())
            .saving(0)
            .build();
    }

}
