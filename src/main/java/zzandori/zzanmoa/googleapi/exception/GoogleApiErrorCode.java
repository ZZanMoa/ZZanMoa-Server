package zzandori.zzanmoa.googleapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import zzandori.zzanmoa.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum GoogleApiErrorCode implements ErrorCode {
    UNSUPPORTED_ENCODING(HttpStatus.BAD_REQUEST, "URL 인코딩에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
