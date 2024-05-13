package zzandori.zzanmoa.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import zzandori.zzanmoa.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum TestErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "우리는 짱모아가 아니라 짠모아입니당...");

    private final HttpStatus httpStatus;
    private final String message;

}
