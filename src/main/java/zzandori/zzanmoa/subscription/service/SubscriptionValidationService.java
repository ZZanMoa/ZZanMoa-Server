package zzandori.zzanmoa.subscription.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import zzandori.zzanmoa.exception.subscription.SubscriptionErrorCode;
import zzandori.zzanmoa.subscription.dto.ValidationError;
import zzandori.zzanmoa.subscription.dto.ValidationErrorResponse;

@Service
@RequiredArgsConstructor
public class SubscriptionValidationService {

    public ResponseEntity<?> validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ValidationError> validationErrors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    SubscriptionErrorCode errorCode = SubscriptionErrorCode.valueOf(fieldError.getDefaultMessage());
                    return ValidationError.builder()
                        .error(errorCode.name())
                        .message(errorCode.getMessage())
                        .build();
                })
                .collect(Collectors.toList());

            ValidationErrorResponse response = ValidationErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errors(validationErrors)
                .build();

            return ResponseEntity.badRequest().body(response);
        }

        return null;
    }

}
