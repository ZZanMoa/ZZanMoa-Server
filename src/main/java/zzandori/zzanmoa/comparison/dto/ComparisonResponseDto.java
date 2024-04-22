package zzandori.zzanmoa.comparison.dto;

import java.util.List;
import java.util.Queue;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ComparisonResponseDto {

    private List<ItemDto> itemList;
    private Queue<RankDto> rankList;

    @Builder
    public ComparisonResponseDto(List<ItemDto> itemList, Queue<RankDto> rankList) {
        this.itemList = itemList;
        this.rankList = rankList;
    }
}
