package zzandori.zzanmoa.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ValidationError {
    private String error;
    private String message;
}

