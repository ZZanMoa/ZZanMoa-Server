package zzandori.zzanmoa.comparison.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MarketPlaceDto {
    private String marketId;
    private String marketName;

    @Builder
    public MarketPlaceDto(String marketId, String marketName) {
        this.marketId = marketId;
        this.marketName = marketName;
    }
}
