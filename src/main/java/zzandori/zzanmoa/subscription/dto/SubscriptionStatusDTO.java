package zzandori.zzanmoa.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class SubscriptionStatusDTO {
    private HttpStatus status;
    private String message;
}
