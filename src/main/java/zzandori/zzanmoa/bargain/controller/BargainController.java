package zzandori.zzanmoa.bargain.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.bargain.service.BargainService;

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
}
