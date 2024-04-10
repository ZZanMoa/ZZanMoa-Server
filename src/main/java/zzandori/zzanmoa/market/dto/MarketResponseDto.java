package zzandori.zzanmoa.market.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MarketResponseDto {

    private Map<String, Set<String>> marketMap;

    public MarketResponseDto() {
        this.marketMap = new HashMap<>();
    }

    public void addMarket(String district, String marketName) {
        this.marketMap.computeIfAbsent(district, k -> new HashSet<>()).add(marketName);
    }

}
