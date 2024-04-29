package zzandori.zzanmoa.bargainboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class DistrictResponseDTO {
    private Integer districtId;
    private String districtName;
}
