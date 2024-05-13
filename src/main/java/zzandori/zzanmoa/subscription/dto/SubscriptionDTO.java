package zzandori.zzanmoa.subscription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "NAME_IS_EMPTY")
    private String name;

    @NotBlank(message = "EMAIL_IS_EMPTY")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "EMAIL_FORMAT_FAILED")
    private String email;

    @NotEmpty(message = "DISTRICT_IS_EMPTY")
    private List<String> district;
}
