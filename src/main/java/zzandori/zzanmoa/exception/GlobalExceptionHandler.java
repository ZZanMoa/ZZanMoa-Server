package zzandori.zzanmoa.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import zzandori.zzanmoa.test.TestException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TestException.class)
    protected ResponseEntity<ErrorResponse> handleTestException(TestException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
            .status(errorCode.getHttpStatus().value())
            .body(new ErrorResponse(errorCode));
    }

}
