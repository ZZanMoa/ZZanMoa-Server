package zzandori.zzanmoa.livingcost.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.livingcost.entity.LivingCost;
import zzandori.zzanmoa.livingcost.repository.LivingCostRepository;
import zzandori.zzanmoa.openapi.service.OpenAPIService;

@Service
@RequiredArgsConstructor
public class LivingCostService {

    private final OpenAPIService openApiService;
    private final LivingCostRepository livingCostRepository;

    private final LivingCostDataProcessor livingCostDataProcessor;

    public String connectOpenAPI(String startIndex, String endIndex) throws IOException {
        StringBuilder sb = openApiService.initRequest("VwNotice", startIndex,
            endIndex);

        return openApiService.connectOpenAPI(sb);
    }

    public void processAndLivingCostData(String json) throws IOException {
        List<LivingCost> livingCostList = processJsonToLivingCostList(json);
        System.out.println(livingCostList.toString());
        saveLivingCost(livingCostList);
    }

    private List<LivingCost> processJsonToLivingCostList(String json) throws IOException {
        return livingCostDataProcessor.parseJsonToLivingCostList(json);
    }

    private void saveLivingCost(List<LivingCost> livingCostList) {
        if(!livingCostList.isEmpty()) {
            livingCostRepository.saveAll(livingCostList);
        }
    }

}
