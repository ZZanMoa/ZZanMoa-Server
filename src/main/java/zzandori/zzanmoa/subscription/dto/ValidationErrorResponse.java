package zzandori.zzanmoa.subscription.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ValidationErrorResponse {
    private LocalDateTime timestamp;
    private int statusCode;
    private List<ValidationError> errors;
}
