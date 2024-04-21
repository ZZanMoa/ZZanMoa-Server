package zzandori.zzanmoa.shoppingcart.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShoppingCartResponseDto {

    private String itemId;
    private String itemName;

    @Builder
    public ShoppingCartResponseDto(String itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }
}
