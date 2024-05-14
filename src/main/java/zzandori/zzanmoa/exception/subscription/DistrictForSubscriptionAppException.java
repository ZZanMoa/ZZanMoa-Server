package zzandori.zzanmoa.exception.subscription;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DistrictForSubscriptionAppException extends RuntimeException{
    private final SubscriptionErrorCode errorCode;
    private final List<String> districtName;
}
