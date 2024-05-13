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
    SAVE_DATA_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "구독 내역 저장에 실패했습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "구독 정보 유효성 검사에 실패했습니다."),
    NAME_IS_EMPTY(HttpStatus.BAD_REQUEST, "이름 입력은 필수 입니다."),
    EMAIL_IS_EMPTY(HttpStatus.BAD_REQUEST, "이메일 입력은 필수 입니다."),
    DISTRICT_IS_EMPTY(HttpStatus.BAD_REQUEST, "자치구 선택은 필수 입니다."),
    EMAIL_FORMAT_FAILED(HttpStatus.BAD_REQUEST, "이메일 형식에 맞지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
