package zzandori.zzanmoa.market.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.repository.MarketRepository;
import zzandori.zzanmoa.openapi.utility.JsonUtil;

@Service
@RequiredArgsConstructor
public class MarketDataProcessor {

    private final MarketRepository marketRepository;
    private final JsonUtil jsonUtil;


    public List<Market> parseJsonToMarketList(String json) throws IOException {
        JsonNode rootNode = jsonUtil.parseJson(json);
        JsonNode rows = rootNode.path("ListNecessariesPricesService").path("row");
        List<Market> marketList = new ArrayList<>();

        if (rows.isArray()) {
            for (JsonNode row : rows) {
                Market market = createMarketIfNotDuplicated(row, marketList);
                if (market != null) {
                    marketList.add(market);
                }
            }
        }
        return marketList;
    }

    private Market createMarketIfNotDuplicated(JsonNode row, List<Market> marketList) {
        String marketId = row.get("M_SEQ").asText().split("\\.")[0];
        String itemId = row.get("A_SEQ").asText().split("\\.")[0];

        if (!isDuplicateInDatabase(marketId, itemId) && !isDuplicateInList(marketId, itemId,
            marketList)) {
            return buildMarket(row);
        }
        return null;
    }

    private boolean isDuplicateInDatabase(String marketId, String itemId) {
        Optional<Market> existingMarket = marketRepository.findByMarketIdAndItemId(marketId,
            itemId);
        return existingMarket.isPresent();
    }

    private boolean isDuplicateInList(String marketId, String itemId, List<Market> marketList) {
        return marketList.stream().anyMatch(
            market -> market.getMarketId().equals(marketId) && market.getItemId().equals(itemId));
    }

    private Market buildMarket(JsonNode row) {
        return Market.builder()
            .marketId(row.get("M_SEQ").asText().split("\\.")[0])
            .marketName(row.get("M_NAME").asText())
            .itemId(row.get("A_SEQ").asText().split("\\.")[0])
            .itemName(row.get("A_NAME").asText())
            .price(Integer.parseInt(row.get("A_PRICE").asText()))
            .date(parseToDate(row.get("P_YEAR_MONTH").asText()))
            .districtName(row.get("M_GU_NAME").asText())
            .build();
    }

    private Date parseToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date + "-01", formatter);

        return java.sql.Date.valueOf(localDate);
    }
}
