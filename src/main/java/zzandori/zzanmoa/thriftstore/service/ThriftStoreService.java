package zzandori.zzanmoa.thriftstore.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.openapi.service.OpenAPIService;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;
import zzandori.zzanmoa.thriftstore.repository.ThriftStoreRepository;

@RequiredArgsConstructor
@Service
public class ThriftStoreService {

    private final OpenAPIService openApiService;
    private final ThriftDataProcessor thriftDataProcessor;
    private final ThriftStoreRepository thriftStoreRepository;

    public String connectOpenAPI(String startIndex, String endIndex) throws IOException {
        StringBuilder sb = openApiService.initRequest("IndividualServiceChargeService", startIndex,
            endIndex);

        return openApiService.connectOpenAPI(sb);
    }


}

