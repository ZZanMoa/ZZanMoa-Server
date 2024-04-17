package zzandori.zzanmoa.thriftstore.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.openapi.utility.JsonUtil;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;
import zzandori.zzanmoa.thriftstore.repository.ThriftStoreRepository;

@Service
@RequiredArgsConstructor
public class ThriftDataProcessor {

    private final ThriftStoreRepository thriftStoreRepository;
    private final JsonUtil jsonUtil;


    public List<ThriftStore> parseJsonToStoreList(String json) throws IOException {
        JsonNode rootNode = jsonUtil.parseJson(json);
        JsonNode rows = rootNode.path("IndividualServiceChargeService").path("row");
        List<ThriftStore> storeList = new ArrayList<>();

        if (rows.isArray()) {
            for (JsonNode row : rows) {
                ThriftStore store = createStoreIfNotDuplicated(row, storeList);
                if (store != null) {
                    storeList.add(store);
                }
            }
        }
        return storeList;
    }

    private ThriftStore createStoreIfNotDuplicated(JsonNode row, List<ThriftStore> storeList) {
        String storeId = row.get("BSSH_SEQ_NO").asText().split("\\.")[0];
        String itemId = row.get("PRDLST_CODE_SE").asText().split("\\.")[0];

        if (!isDuplicateInDatabase(storeId, itemId) && !isDuplicateInList(storeId, itemId, storeList)) {
            return buildMarket(row);
        }
        return null;
    }

    private boolean isDuplicateInDatabase(String storeId, String itemId) {
        Optional<ThriftStore> existingStore = thriftStoreRepository.findByStoreIdAndItemId(storeId,
            itemId);
        return existingStore.isPresent();
    }

    private boolean isDuplicateInList(String storeId, String itemId, List<ThriftStore> storeList) {
        return storeList.stream().anyMatch(
            store -> store.getStoreId().equals(storeId) && store.getItemId().equals(itemId));
    }

    private ThriftStore buildMarket(JsonNode row) {
        return ThriftStore.builder()
            .storeId(String.valueOf((new BigDecimal(row.get("BSSH_SEQ_NO").asText())).longValueExact()))
            .storeName(row.get("BSSH_NM").asText())
            .category(row.get("INDUTY_DESC").asText())
            .itemId(row.get("PRDLST_CODE_SE").asText())
            .itemName(row.get("PRDLST_DESC").asText())
            .price(Integer.parseInt((row.get("PC").asText()).replace(".", "")))
            .phoneNumber(row.get("TLPHON_NO_CN").asText())
            .address(row.get("ADRES_CN2").asText())
            .build();
    }

}
