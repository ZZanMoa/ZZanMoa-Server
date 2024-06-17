package zzandori.zzanmoa.googleapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import zzandori.zzanmoa.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class GoogleApiException extends RuntimeException {

    private final ErrorCode errorCode;
}
