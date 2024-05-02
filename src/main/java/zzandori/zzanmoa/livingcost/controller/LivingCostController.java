package zzandori.zzanmoa.livingcost.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.livingcost.service.LivingCostService;

@Tag(name = "LivingCostController", description = "서울시 물가 소식정보를 저장하는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/living-cost")
public class LivingCostController {

    private final LivingCostService livingCostService;

    @GetMapping("/connectAPI/{startIndex}/{endIndex}")
    public void connectOpenAPI(@PathVariable("startIndex") String startIndex,
        @PathVariable("endIndex") String endIndex) throws IOException {

        String json = livingCostService.connectOpenAPI(startIndex, endIndex);
        livingCostService.processAndLivingCostData(json);
    }

}
