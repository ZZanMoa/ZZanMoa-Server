package zzandori.zzanmoa.item.service;

import java.util.List;
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

        markets.stream()
            .collect(Collectors.toMap(Market::getItemId, Function.identity(),
                (existing, replacement) -> existing))
            .values()
            .forEach(market -> saveItem(market));
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
