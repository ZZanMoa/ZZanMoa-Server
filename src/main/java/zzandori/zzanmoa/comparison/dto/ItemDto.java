package zzandori.zzanmoa.comparison.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemDto {

    private String itemId;
    private String itemName;
    private int average_price;

    @Builder

    public ItemDto(String itemId, String itemName, int average_price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.average_price = average_price;
    }
}
