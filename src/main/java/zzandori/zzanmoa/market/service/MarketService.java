package zzandori.zzanmoa.market.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.market.dto.MarketResponseDto;
import zzandori.zzanmoa.openapi.service.OpenAPIService;

@RequiredArgsConstructor
@Service
public class MarketService {

    private final OpenAPIService openApiService;

    public MarketResponseDto getMarkets() throws IOException {

        return connectOpenAPI();
    }

    private MarketResponseDto connectOpenAPI() throws IOException {
        StringBuilder sb = openApiService.initRequest("ListNecessariesPricesService", "1", "1000");
        return convertToDto(openApiService.connectOpenAPI(sb));
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
