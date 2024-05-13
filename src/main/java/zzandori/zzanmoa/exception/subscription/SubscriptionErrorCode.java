package zzandori.zzanmoa.exception.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import zzandori.zzanmoa.exception.ErrorCode;

@AllArgsConstructor
@Getter
public enum SubscriptionErrorCode implements ErrorCode {

    DISTRICT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 자치구 입니다."),
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 구독 정보 입니다."),
    SUBSCRIPTION_DUPLICATED(HttpStatus.CONFLICT, "이미 구독한 내역이 존재합니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
    SAVE_DATA_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "구독 내역 저장에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
