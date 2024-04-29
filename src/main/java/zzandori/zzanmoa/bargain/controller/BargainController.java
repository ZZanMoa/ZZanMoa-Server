package zzandori.zzanmoa.bargain.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.bargain.service.BargainService;

@Tag(name = "BargainController", description = "서울시 할인행사/직거래장터 정보를 저장하는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bargain")
public class BargainController {

    private final BargainService bargainService;

    @GetMapping("/connectAPI/{startIndex}/{endIndex}")
    public void connectOpenAPI(@PathVariable("startIndex") String startIndex,
        @PathVariable("endIndex") String endIndex) throws IOException {

        String json = bargainService.connectOpenAPI(startIndex, endIndex);
        bargainService.processAndStoreData(json);
    }

    @GetMapping("/update/district")
    public void updateDistrict(){
        bargainService.updateDistrictNameInBargains();
    }
}
