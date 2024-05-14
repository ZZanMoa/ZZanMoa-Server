package zzandori.zzanmoa.exception.subscription;

import java.util.List;
import lombok.Getter;
import zzandori.zzanmoa.exception.ErrorCode;
import zzandori.zzanmoa.exception.ErrorResponse;

@Getter
public class DistrictDuplicatedErrorResponse extends ErrorResponse {
    private final List<String> district;

    public DistrictDuplicatedErrorResponse(ErrorCode errorCode, List<String> district) {
        super(errorCode);
        this.district = district;
    }
}
