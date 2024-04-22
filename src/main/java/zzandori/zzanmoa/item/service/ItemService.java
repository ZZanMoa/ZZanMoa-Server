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

        Map<String, Item> itemMap = markets.stream()
            .collect(Collectors.groupingBy(
                Market::getItemId,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    (List<Market> list) -> { // 명시적으로 List<Market>로 타입 지정
                        String itemName = list.get(0).getItemName();
                        int averagePrice = (int) Math.round(list.stream()
                            .collect(Collectors.averagingDouble(Market::getPrice)));
                        return Item.builder()
                            .itemId(String.valueOf(list.get(0).getItemId()))
                            .itemName(itemName)
                            .averagePrice(averagePrice)
                            .build();
                    }
                )
            ));

        itemMap.values().forEach(item -> itemRepository.save(item));
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    private void saveItem(Market market) {
        Item item = Item.builder()
            .itemId(market.getItemId())
            .itemName(market.getItemName())

            .build();
        itemRepository.save(item);
    }

}
