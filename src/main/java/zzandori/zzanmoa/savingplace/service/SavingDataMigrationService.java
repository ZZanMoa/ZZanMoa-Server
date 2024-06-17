package zzandori.zzanmoa.savingplace.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.dto.geometry.GeocodeResponse;
import zzandori.zzanmoa.googleapi.dto.geometry.Location;
import zzandori.zzanmoa.googleapi.dto.geometry.Result;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.savingplace.entity.SavingItem;
import zzandori.zzanmoa.savingplace.entity.SavingStore;
import zzandori.zzanmoa.savingplace.entity.SavingStoreGoogleIds;
import zzandori.zzanmoa.savingplace.repository.SavingItemRepository;
import zzandori.zzanmoa.savingplace.repository.SavingStoreGoogleIdsRepository;
import zzandori.zzanmoa.savingplace.repository.SavingStoreRepository;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;
import zzandori.zzanmoa.thriftstore.repository.ThriftStoreRepository;

@RequiredArgsConstructor
@Service
public class SavingDataMigrationService {

    private final ThriftStoreRepository thriftStoreRepository;
    private final SavingStoreRepository savingStoreRepository;
    private final SavingItemRepository savingItemRepository;
    private final SavingStoreGoogleIdsRepository savingStoreGoogleIdsRepository;
    private final GoogleMapApiService googleMapApiService;


    public void save() {
        List<ThriftStore> thriftStores = thriftStoreRepository.findAll();
        Map<String, SavingStore> storeMap = new HashMap<>();
        Map<String, String> placeIdMap = new HashMap<>();

        thriftStores.forEach(thriftStore -> processThriftStore(thriftStore, storeMap, placeIdMap));

        persistStoresAndItems(storeMap, placeIdMap);
    }

    private void processThriftStore(ThriftStore thriftStore, Map<String, SavingStore> storeMap, Map<String, String> placeIdMap) {
        if (thriftStore.getAddress().isEmpty()) {
            return;
        }

        storeMap.computeIfAbsent(thriftStore.getStoreId(), k -> {
            return createAndMapSavingStore(thriftStore, placeIdMap);
        });

        if (thriftStore.getPrice() != 0) {
            addSavingItemToStore(thriftStore, storeMap.get(thriftStore.getStoreId()));
        }
    }

    private SavingStore createAndMapSavingStore(ThriftStore thriftStore, Map<String, String> placeIdMap) {
        GeocodeResponse geocodeResponse = googleMapApiService.requestGeocode(
            thriftStore.getAddress());
        Result result = geocodeResponse.getResults().get(0);

        String placeId = result.getPlace_id();
        placeIdMap.put(thriftStore.getStoreId(), placeId);

        Location location = result.getGeometry().getLocation();
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

    private void addSavingItemToStore(ThriftStore thriftStore, SavingStore savingStore) {
        SavingItem savingItem = SavingItem.builder()
            .itemId(thriftStore.getItemId())
            .itemName(thriftStore.getItemName())
            .category(thriftStore.getCategory())
            .price(thriftStore.getPrice())
            .store(savingStore)
            .build();
        savingStore.getItems().add(savingItem);
    }

    private void persistStoresAndItems(Map<String, SavingStore> storeMap, Map<String, String> placeIdMap) {
        storeMap.values().forEach(savingStore -> {
            savingStoreRepository.save(savingStore);
            savingStoreGoogleIdsRepository.save(buildSavingStoreGoogleIds(placeIdMap.get(savingStore.getStoreId()), savingStore));
            savingItemRepository.saveAll(savingStore.getItems());
        });
    }

    private SavingStoreGoogleIds buildSavingStoreGoogleIds(String placeId,
        SavingStore savingStore) {
        return SavingStoreGoogleIds.builder()
            .placeId(placeId)
            .savingStore(savingStore)
            .build();
    }
}
