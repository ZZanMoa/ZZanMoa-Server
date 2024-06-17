package zzandori.zzanmoa.savingplace.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SavingStoreReviewResponseDto {
    private List<String> reviews;
}
