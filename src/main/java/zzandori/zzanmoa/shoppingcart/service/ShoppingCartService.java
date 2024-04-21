package zzandori.zzanmoa.shoppingcart.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.item.entity.Item;
import zzandori.zzanmoa.item.service.ItemService;
import zzandori.zzanmoa.shoppingcart.dto.ShoppingCartResponseDto;

@RequiredArgsConstructor
@Service
public class ShoppingCartService {

    private final ItemService itemService;

    public List<ShoppingCartResponseDto> getItems() {
        List<Item> items = itemService.getItems();

        return items.stream()
            .map(item -> ShoppingCartResponseDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .build()) // 빌더를 사용하여 DTO 객체 생성
            .collect(Collectors.toList());
    }
}
