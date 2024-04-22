package zzandori.zzanmoa.item.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.item.service.ItemService;
import zzandori.zzanmoa.shoppingcart.dto.ShoppingCartResponseDto;
import zzandori.zzanmoa.shoppingcart.service.ShoppingCartService;

@Tag(name = "ItemController", description = "시장의 품목 정보 응답 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/market/item")
public class ItemController {

    private final ItemService itemService;
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/save")
    public void saveGroceries() {
        itemService.saveItems();
    }

    @RequestMapping("/get")
    public ResponseEntity<List<ShoppingCartResponseDto>> getItems() {
        List<ShoppingCartResponseDto> shoppingCartResponseDtos = shoppingCartService.getItems();
        return ResponseEntity.ok(shoppingCartResponseDtos);
    }

}
