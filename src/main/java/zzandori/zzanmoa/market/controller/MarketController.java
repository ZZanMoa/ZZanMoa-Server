package zzandori.zzanmoa.market.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.market.service.MarketService;

@Tag(name = "MarketController", description = "서울시 생필품 농수축산물 가격 정보를 저장하는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/connectAPI/{startIndex}/{endIndex}")
    public void connectOpenAPI(@PathVariable("startIndex") String startIndex,
        @PathVariable("endIndex") String endIndex) throws IOException {

        String json = marketService.connectOpenAPI(startIndex, endIndex);
        marketService.processAndStoreMarketData(json);
    }
}
