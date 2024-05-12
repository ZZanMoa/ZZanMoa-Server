package zzandori.zzanmoa.subscription.dto;

import java.util.List;
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
public class SubscriptionDTO {
    private String name;
    private String email;
    private List<String> district;
}
