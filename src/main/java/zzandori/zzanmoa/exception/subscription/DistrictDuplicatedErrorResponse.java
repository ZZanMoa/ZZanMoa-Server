package zzandori.zzanmoa.exception.subscription;

import lombok.Getter;
import zzandori.zzanmoa.exception.ErrorCode;
import zzandori.zzanmoa.exception.ErrorResponse;

@Getter
public class DistrictDuplicatedErrorResponse extends ErrorResponse {
    private final String district;

    public DistrictDuplicatedErrorResponse(ErrorCode errorCode, String district) {
        super(errorCode);
        this.district = district;
    }
}
