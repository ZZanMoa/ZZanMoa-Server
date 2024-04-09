package zzandori.zzanmoa.market.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.market.dto.MarketResponseDto;
import zzandori.zzanmoa.market.service.MarketService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/get")
    public MarketResponseDto getMarkets() throws IOException {
        return marketService.getMarkets();
    }
}
