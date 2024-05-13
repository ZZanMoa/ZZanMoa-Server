package zzandori.zzanmoa.common.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class SuccessResponseDTO {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private int statusCode;
    private String message;
}
