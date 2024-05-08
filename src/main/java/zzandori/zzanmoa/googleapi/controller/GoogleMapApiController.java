package zzandori.zzanmoa.googleapi.controller;

import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;

@RestController
@RequiredArgsConstructor
public class GoogleMapApiController {

    private final GoogleMapApiService googleMapApiService;

    @PostMapping("/google-map-api")
    public Mono<String> doTest(@RequestBody String address) throws UnsupportedEncodingException {
        return googleMapApiService.requestTest(address);
    }
}
