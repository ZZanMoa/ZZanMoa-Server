package zzandori.zzanmoa.exception.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import zzandori.zzanmoa.exception.ErrorCode;

@AllArgsConstructor
@Getter
public class SubscriptionAppException extends RuntimeException{
    private final ErrorCode errorCode;
}
