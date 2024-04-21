package zzandori.zzanmoa.shoppingcart.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShoppingCartResponseDto {

    private List<String> groceries;

    @Builder
    public ShoppingCartResponseDto(List<String> groceries) {
        this.groceries = groceries;
    }
}
