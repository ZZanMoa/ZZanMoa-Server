package zzandori.zzanmoa.marketplace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MarketPlaceResponseDto {
    private String marketId;
    private String marketName;

    @Builder
    public MarketPlaceResponseDto(String marketId, String marketName) {
        this.marketId = marketId;
        this.marketName = marketName;
    }
}
