package zzandori.zzanmoa.savingplace.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.dto.Location;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.savingplace.entity.SavingItem;
import zzandori.zzanmoa.savingplace.entity.SavingStore;
import zzandori.zzanmoa.savingplace.repository.SavingItemRepository;
import zzandori.zzanmoa.savingplace.repository.SavingStoreRepository;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;
import zzandori.zzanmoa.thriftstore.repository.ThriftStoreRepository;

@RequiredArgsConstructor
@Service
public class SavingDataMigrationService {

    private final ThriftStoreRepository thriftStoreRepository;
    private final SavingStoreRepository savingStoreRepository;
    private final SavingItemRepository savingItemRepository;
    private final GoogleMapApiService googleMapApiService;


    public void save() throws InterruptedException, UnsupportedEncodingException {
        List<ThriftStore> thriftStores = thriftStoreRepository.findAll();
        Map<String, SavingStore> storeMap = new HashMap<>();

        thriftStores.forEach(thriftStore -> processThriftStore(thriftStore, storeMap));

        storeMap.values().forEach(this::persistStoresAndItems);
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
}
