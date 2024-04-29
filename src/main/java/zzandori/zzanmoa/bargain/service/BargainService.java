package zzandori.zzanmoa.bargain.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.bargain.entity.Bargain;
import zzandori.zzanmoa.bargain.entity.District;
import zzandori.zzanmoa.bargain.respository.BargainRepository;
import zzandori.zzanmoa.openapi.service.OpenAPIService;

@RequiredArgsConstructor
@Service
public class BargainService {

    private final OpenAPIService openApiService;
    private final BargainDataProcessor bargainDataProcessor;
    private final BargainRepository bargainRepository;

    public String connectOpenAPI(String startIndex, String endIndex) throws IOException {
        StringBuilder sb = openApiService.initRequest("VwNews", startIndex,
            endIndex);

        return openApiService.connectOpenAPI(sb);
    }

    public void processAndStoreData(String json) throws IOException {
        List<Bargain> bargainList = processJsonToStoreList(json);
        System.out.println(bargainList.toString());
        saveBargainList(bargainList);
    }

    private List<Bargain> processJsonToStoreList(String json) throws IOException {
        return bargainDataProcessor.parseJsonToStoreList(json);
    }

    private void saveBargainList(List<Bargain> bargainList){
        if(!bargainList.isEmpty()){
            bargainRepository.saveAll(bargainList);
        }
    }

    public void updateDistrictNameInBargains() {
        List<Bargain> bargains = bargainRepository.findAll();
        for (Bargain bargain : bargains) {
            Optional<District> matchingDistrict = Arrays.stream(District.values())
                .filter(d -> d.getDistrictId() == bargain.getDistrictId())
                .findFirst();
            if (matchingDistrict.isPresent()) {
                bargain.setDistrictName(matchingDistrict.get().getDistrictName());
                bargainRepository.save(bargain);
            }
        }
    }
}
