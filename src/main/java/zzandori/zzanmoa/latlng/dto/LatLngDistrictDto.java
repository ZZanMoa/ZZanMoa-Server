package zzandori.zzanmoa.latlng.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LatLngDistrictDto {

    private String district;
    private List<LatLngSubDistrictDto> subDistricts;

    @Builder
    public LatLngDistrictDto(String district, List<LatLngSubDistrictDto> subDistricts) {
        this.district = district;
        this.subDistricts = subDistricts;
    }
}
