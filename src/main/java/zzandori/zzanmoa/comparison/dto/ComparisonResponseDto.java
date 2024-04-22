package zzandori.zzanmoa.comparison.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ComparisonResponseDto {

    private List<ItemDto> itemList;
    private List<RankDto> rankList;

    @Builder
    public ComparisonResponseDto(List<ItemDto> itemList, List<RankDto> rankList) {
        this.itemList = itemList;
        this.rankList = rankList;
    }
}
