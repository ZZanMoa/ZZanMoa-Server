package zzandori.zzanmoa.market.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.market.dto.MarketResponseDto;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.repository.MarketRepository;
import zzandori.zzanmoa.openapi.service.OpenAPIService;

@RequiredArgsConstructor
@Service
public class MarketService {

    private final OpenAPIService openApiService;
    private final MarketRepository marketRepository;

    public void getMarkets() throws IOException {

    }

    public void connectOpenAPI(String startIndex, String endIndex) throws IOException {
        StringBuilder sb = openApiService.initRequest("ListNecessariesPricesService", startIndex,
            endIndex);
        sb.append("/").append("/").append("/" + URLEncoder.encode("2024-03", "UTF-8"));
        convertToEntity(openApiService.connectOpenAPI(sb));
    }

    private void convertToEntity(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        JsonNode rows = rootNode.path("ListNecessariesPricesService").path("row");

        if (rows.isArray()) {
            List<Market> marketList = new ArrayList<>();

            for (JsonNode row : rows) {
                String marketId = row.get("M_SEQ").asText().split("\\.")[0];
                String itemId = row.get("A_SEQ").asText().split("\\.")[0];

                // DB에서 유니크 제약 조건 위반 검사
                Optional<Market> existingMarket = marketRepository.findByMarketIdAndItemId(marketId,
                    itemId);
                if (existingMarket.isEmpty()) {
                    // marketList에서 중복 검사
                    boolean isDuplicateInList = marketList.stream().anyMatch(market ->
                        market.getMarketId().equals(marketId) && market.getItemId().equals(itemId));
                    if (!isDuplicateInList) { // 중복이 없으면 추가
                        String marketName = row.get("M_NAME").asText();
                        String itemName = row.get("A_NAME").asText();
                        int price = Integer.parseInt(row.get("A_PRICE").asText());
                        Date date = parseToDate(row.get("P_YEAR_MONTH").asText());
                        String district = row.get("M_GU_NAME").asText();

                        Market market = Market.builder()
                            .marketId(marketId)
                            .marketName(marketName)
                            .itemId(itemId)
                            .itemName(itemName)
                            .price(price)
                            .date(date)
                            .districtName(district)
                            .build();

                        marketList.add(market);
                    }
                }
                // DB에서도 중복되지 않고, 리스트 내에서도 중복되지 않은 경우에만 추가됩니다.
            }
            if (!marketList.isEmpty()) {
                marketRepository.saveAll(marketList); // 검사 후 남은 엔터티들 저장
            }
        }
    }


    private Date parseToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date + "-01", formatter);

        return Date.valueOf(localDate);
    }

    private MarketResponseDto convertToDto(String json) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        JsonNode rows = rootNode.path("ListNecessariesPricesService").path("row");

        MarketResponseDto marketResponseDto = new MarketResponseDto();

        if (rows.isArray()) {
            for (JsonNode row : rows) {
                System.out.println(row);
                String marketName = row.get("M_NAME").asText();
                String district = row.get("M_GU_NAME").asText();
                marketResponseDto.addMarket(district, marketName);
            }
        }

        return marketResponseDto;
    }


}
