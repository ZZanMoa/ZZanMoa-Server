package zzandori.zzanmoa.thriftstore.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.openapi.service.OpenAPIService;
import zzandori.zzanmoa.thriftstore.dto.CategoryPriceDTO;
import zzandori.zzanmoa.thriftstore.dto.ItemInfoDTO;
import zzandori.zzanmoa.thriftstore.dto.StoreInfoDTO;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;
import zzandori.zzanmoa.thriftstore.repository.SavingItemRepository;
import zzandori.zzanmoa.thriftstore.repository.SavingStoreRepository;
import zzandori.zzanmoa.thriftstore.repository.ThriftStoreRepository;

@RequiredArgsConstructor
@Service
public class ThriftStoreService {

    private final OpenAPIService openApiService;
    private final ThriftDataProcessor thriftDataProcessor;
    private final ThriftStoreRepository thriftStoreRepository;
    private final SavingStoreRepository savingStoreRepository;
    private final SavingItemRepository savingItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(ThriftStoreService.class);


    public List<CategoryPriceDTO> getCategoryPrice(){
        List<CategoryPriceDTO> results = savingItemRepository.findCategoryPrices();
        logger.info("Fetched category prices size: {}", results.size());
        return results;
    }

    public List<StoreInfoDTO> getAllStoresWithItems() {
        return savingStoreRepository.findAllStoreWithItems().stream()
            .map(store -> new StoreInfoDTO(
                store.getStoreId(),
                store.getStoreName(),
                store.getPhoneNumber(),
                store.getAddress(),
                store.getItems().stream()
                    .map(item -> new ItemInfoDTO(item.getItemId(), item.getItemName(), item.getCategory(), item.getPrice()))
                    .collect(Collectors.toList())))
            .collect(Collectors.toList());
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

