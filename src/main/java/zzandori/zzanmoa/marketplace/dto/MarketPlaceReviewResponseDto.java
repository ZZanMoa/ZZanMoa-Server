package zzandori.zzanmoa.marketplace.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MarketPlaceReviewResponseDto {
    List<String> reviews;

}
