package zzandori.zzanmoa.market.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MarketResponseDto {

    private List<String> market;

    public MarketResponseDto() {
        this.market = new ArrayList<>();
    }

    public void addMarket(List<String> marketName) {
        this.market.addAll(marketName);
    }

}
