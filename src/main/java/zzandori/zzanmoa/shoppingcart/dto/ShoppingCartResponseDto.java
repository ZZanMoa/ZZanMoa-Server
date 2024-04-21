package zzandori.zzanmoa.shoppingcart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ShoppingCartResponseDto {

    String groceryName;

    @Builder
    public ShoppingCartResponseDto(String groceryName) {
        this.groceryName = groceryName;
    }
}
