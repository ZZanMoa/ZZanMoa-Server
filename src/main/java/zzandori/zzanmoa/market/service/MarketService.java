package zzandori.zzanmoa.market.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.market.entity.Market;
import zzandori.zzanmoa.market.repository.MarketRepository;
import zzandori.zzanmoa.openapi.service.OpenAPIService;

@RequiredArgsConstructor
@Service
public class MarketService {

    private final OpenAPIService openApiService;
    private final MarketRepository marketRepository;
    private final MarketDataProcessor marketDataProcessor;


    public String connectOpenAPI(String startIndex, String endIndex) throws IOException {
        StringBuilder sb = openApiService.initRequest("ListNecessariesPricesService", startIndex,
            endIndex);
        sb.append("/").append("/").append("/").append(URLEncoder.encode("2024-03",
            StandardCharsets.UTF_8));

        return openApiService.connectOpenAPI(sb);
    }

    public void processAndStoreMarketData(String json) throws IOException {
        List<Market> marketList = processJsonToMarketList(json);
        saveMarketList(marketList);
    }

    public List<Market> getMarketsByNames(List<String> marketName) {
        return marketRepository.findMarketsByNames(marketName);
    }

    public List<Market> getMarketsByNamesAndItems(List<String> marketName, List<String> itemName) {
        return marketRepository.findMarketsByNamesAndItems(marketName, itemName);
    }

    private List<Market> processJsonToMarketList(String json) throws IOException {
        return marketDataProcessor.parseJsonToMarketList(json);
    }

    private void saveMarketList(List<Market> marketList) {
        if (!marketList.isEmpty()) {
            marketRepository.saveAll(marketList);
        }
    }

}
