package zzandori.zzanmoa.comparison.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SavingDto {

    private MarketItemDto marketItem;
    private int saving;

    @Builder
    public SavingDto(MarketItemDto marketItem, int saving) {
        this.marketItem = marketItem;
        this.saving = saving;
    }
}
