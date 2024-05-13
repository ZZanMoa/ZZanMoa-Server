package zzandori.zzanmoa.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import zzandori.zzanmoa.exception.subscription.SubscriptionAppException;
import zzandori.zzanmoa.exception.subscription.SubscriptionErrorCode;

@Service
@RequiredArgsConstructor
public class SubscriptionValidationService {

    public void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                FieldError fieldError = (FieldError) error;
                String field = fieldError.getField();
                String errorCode = fieldError.getDefaultMessage();
                throw new SubscriptionAppException(SubscriptionErrorCode.valueOf(errorCode));
            });
        }
    }

}
