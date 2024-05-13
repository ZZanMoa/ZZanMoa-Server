package zzandori.zzanmoa.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import zzandori.zzanmoa.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class TestException extends RuntimeException {

    private final ErrorCode errorCode;

}
