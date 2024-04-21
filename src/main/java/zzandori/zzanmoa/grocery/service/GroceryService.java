package zzandori.zzanmoa.grocery.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.grocery.entity.Grocery;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.repository.MarketRepository;
import zzandori.zzanmoa.grocery.repository.GroceryRepository;

@RequiredArgsConstructor
@Service
public class GroceryService {

    private final MarketRepository marketRepository;
    private final GroceryRepository groceryRepository;

    public void saveGroceries() {
        List<Market> markets = marketRepository.findAll();

        markets.stream()
            .collect(Collectors.toMap(Market::getItemId, Function.identity(), (existing, replacement) -> existing))
            .values()
            .forEach(market -> saveGrocery(market));
    }

    public List<Grocery> getGroceries() {
        return groceryRepository.findAll();
    }

    private void saveGrocery(Market market) {
        Grocery grocery = Grocery.builder()
            .itemId(market.getItemId())
            .itemName(market.getItemName())
            .build();
        groceryRepository.save(grocery);
    }

}
