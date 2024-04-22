package zzandori.zzanmoa.comparison.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RankDto {

    private int rank;
    private MarketPlaceDto market;
    private List<SavingDto> savingList;
    private int totalSaving;

    @Builder

    public RankDto(int rank, MarketPlaceDto market, List<SavingDto> savingList, int totalSaving) {
        this.rank = rank;
        this.market = market;
        this.savingList = savingList;
        this.totalSaving = totalSaving;
    }
}
