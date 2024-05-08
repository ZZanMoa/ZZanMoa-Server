package zzandori.zzanmoa.savingplace.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.dto.Location;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO;
import zzandori.zzanmoa.savingplace.dto.ItemInfoDTO;
import zzandori.zzanmoa.savingplace.dto.StoreInfoDTO;
import zzandori.zzanmoa.savingplace.entity.SavingItem;
import zzandori.zzanmoa.savingplace.entity.SavingStore;
import zzandori.zzanmoa.savingplace.repository.SavingItemRepository;
import zzandori.zzanmoa.savingplace.repository.SavingStoreRepository;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;
import zzandori.zzanmoa.thriftstore.repository.ThriftStoreRepository;
import zzandori.zzanmoa.thriftstore.service.ThriftStoreService;

@RequiredArgsConstructor
@Service
public class SavingPlaceService {

    private final ThriftStoreRepository thriftStoreRepository;
    private final SavingStoreRepository savingStoreRepository;
    private final SavingItemRepository savingItemRepository;
    private final GoogleMapApiService googleMapApiService;

    private static final Logger logger = LoggerFactory.getLogger(ThriftStoreService.class);

    public void save() throws InterruptedException, UnsupportedEncodingException {
        List<ThriftStore> thriftStores = thriftStoreRepository.findAll();
        Map<String, SavingStore> storeMap = new HashMap<>();

        thriftStores.forEach(thriftStore -> processThriftStore(thriftStore, storeMap));

        storeMap.values().forEach(this::persistStoresAndItems);
    }

    public List<CategoryPriceDTO> getCategoryPrice() {
        List<CategoryPriceDTO> results = savingItemRepository.findCategoryPrices();
        logger.info("Fetched category prices size: {}", results.size());
        return results;
    }

    public List<StoreInfoDTO> getAllStoresWithItems() {
        return savingStoreRepository.findAllStoreWithItems().stream()
            .map(store -> new StoreInfoDTO(
                store.getStoreId(),
                decodeHtmlEntities(store.getStoreName()),
                store.getPhoneNumber(),
                decodeHtmlEntities(store.getAddress()),
                store.getLatitude(),
                store.getLongitude(),
                store.getItems().stream()
                    .map(item -> new ItemInfoDTO(item.getItemId(), item.getItemName(),
                        item.getCategory(), item.getPrice()))
                    .collect(Collectors.toList())))
            .collect(Collectors.toList());
    }

    private void processThriftStore(ThriftStore thriftStore, Map<String, SavingStore> storeMap) {
        if (thriftStore.getAddress().isEmpty()) {
            return;
        }

        storeMap.computeIfAbsent(thriftStore.getStoreId(), k -> {
            try {
                return createSavingStore(thriftStore);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        });

        if (thriftStore.getPrice() != 0) {
            addSavingItem(thriftStore, storeMap.get(thriftStore.getStoreId()));
        }
    }

    private SavingStore createSavingStore(ThriftStore thriftStore)
        throws UnsupportedEncodingException {
        Location location = googleMapApiService.requestGeocode(thriftStore.getAddress());
        return SavingStore.builder()
            .storeId(thriftStore.getStoreId())
            .storeName(thriftStore.getStoreName())
            .phoneNumber(thriftStore.getPhoneNumber())
            .address(thriftStore.getAddress())
            .latitude(location.getLat())
            .longitude(location.getLng())
            .items(new ArrayList<>())
            .build();
    }

    private void addSavingItem(ThriftStore thriftStore, SavingStore savingStore) {
        SavingItem savingItem = SavingItem.builder()
            .itemId(thriftStore.getItemId())
            .itemName(thriftStore.getItemName())
            .category(thriftStore.getCategory())
            .price(thriftStore.getPrice())
            .store(savingStore)
            .build();
        savingStore.getItems().add(savingItem);
    }

    private void persistStoresAndItems(SavingStore savingStore) {
        savingStoreRepository.save(savingStore);
        savingItemRepository.saveAll(savingStore.getItems());
    }

    private String decodeHtmlEntities(String input) {
        String correctedInput = correctHtmlEntities(input);
        return StringEscapeUtils.unescapeHtml4(correctedInput);
    }

    private String correctHtmlEntities(String input) {
        return input.replaceAll("& #(\\d+)", "&#$1;");
    }
}
