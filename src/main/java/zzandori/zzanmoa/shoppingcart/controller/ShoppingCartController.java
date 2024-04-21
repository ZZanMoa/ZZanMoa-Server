package zzandori.zzanmoa.shoppingcart.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.shoppingcart.dto.ShoppingCartResponseDto;
import zzandori.zzanmoa.shoppingcart.service.ShoppingCartService;

@Tag(name = "ShoppingCartController", description = "장바구니에서 식료품 정보를 반환하는 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @RequestMapping("/get")
    public ResponseEntity<List<ShoppingCartResponseDto>> getItems() {
        List<ShoppingCartResponseDto> shoppingCartResponseDtos = shoppingCartService.getItems();
        return ResponseEntity.ok(shoppingCartResponseDtos);
    }

}
