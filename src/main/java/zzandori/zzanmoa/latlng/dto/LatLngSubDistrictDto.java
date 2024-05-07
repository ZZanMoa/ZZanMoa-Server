package zzandori.zzanmoa.latlng.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LatLngSubDistrictDto {
    private String dong;
    private Double latitude;
    private Double longitude;

    @Builder
    public LatLngSubDistrictDto(String dong, Double latitude, Double longitude) {
        this.dong = dong;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
