package zzandori.zzanmoa.item.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.item.entity.Item;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.repository.MarketRepository;
import zzandori.zzanmoa.item.repository.ItemRepository;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final MarketRepository marketRepository;
    private final ItemRepository itemRepository;

    public void saveItems() {
        List<Market> markets = marketRepository.findAll();
        Map<String, Item> itemMap = createItemMapFromMarkets(markets);
        itemMap.values().forEach(itemRepository::save);
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public Item getItemByItemName(String itemName) {
        return itemRepository.findByItemName(itemName).orElse(null);
    }

    public List<Item> getItemsByItemNames(List<String> itemNames) {
        return itemRepository.findByItemNames(itemNames);
    }

    private Map<String, Item> createItemMapFromMarkets(List<Market> markets) {
        return markets.stream()
            .collect(Collectors.groupingBy(
                Market::getItemId,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    this::createItemFromMarketList
                )
            ));
    }

    private Item createItemFromMarketList(List<Market> markets) {
        String itemName = markets.get(0).getItemName();
        int averagePrice = calculateAveragePrice(markets);
        return Item.builder()
            .itemId(markets.get(0).getItemId())
            .itemName(itemName)
            .averagePrice(averagePrice)
            .build();
    }

    private int calculateAveragePrice(List<Market> markets) {
        return (int) Math.round(markets.stream()
            .collect(Collectors.averagingDouble(Market::getPrice)));
    }

}
