package zzandori.zzanmoa.comparison.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MarketItemDto {

    private String itemId;
    private String itemName;
    private int price;
    private boolean isSale;

    @Builder
    public MarketItemDto(String itemId, String itemName, int price, boolean isSale) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.isSale = isSale;
    }
}
