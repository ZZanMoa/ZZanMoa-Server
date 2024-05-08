package zzandori.zzanmoa.marketplace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MarketPlaceResponseDto {

    private String marketId;
    private String marketName;
    private Double latitude;
    private Double longitude;

    @Builder
    public MarketPlaceResponseDto(String marketId, String marketName, Double latitude,
        Double longitude) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
