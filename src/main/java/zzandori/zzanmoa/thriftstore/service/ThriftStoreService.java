package zzandori.zzanmoa.thriftstore.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.openapi.service.OpenAPIService;
import zzandori.zzanmoa.thriftstore.dto.CategoryPriceDTO;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;
import zzandori.zzanmoa.thriftstore.repository.ThriftStoreRepository;

@RequiredArgsConstructor
@Service
public class ThriftStoreService {

    private final OpenAPIService openApiService;
    private final ThriftDataProcessor thriftDataProcessor;
    private final ThriftStoreRepository thriftStoreRepository;
    private static final Logger logger = LoggerFactory.getLogger(ThriftStoreService.class);


    public List<CategoryPriceDTO> getCategoryPrice(){
        List<CategoryPriceDTO> results = thriftStoreRepository.findCategoryPrices();
        logger.info("Fetched category prices size: {}", results.size());
        return results;
    }

    public String connectOpenAPI(String startIndex, String endIndex) throws IOException {
        StringBuilder sb = openApiService.initRequest("IndividualServiceChargeService", startIndex,
            endIndex);

        return openApiService.connectOpenAPI(sb);
    }

    public void processAndStoreData(String json) throws IOException {
        List<ThriftStore> storeList = processJsonToStoreList(json);
        System.out.println(storeList.toString());
        saveStoreList(storeList);
    }

    private List<ThriftStore> processJsonToStoreList(String json) throws IOException {
        return thriftDataProcessor.parseJsonToStoreList(json);
    }

    private void saveStoreList(List<ThriftStore> storeList) {
        if (!storeList.isEmpty()) {
            thriftStoreRepository.saveAll(storeList);
        }
    }

}

