package zzandori.zzanmoa.thriftstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.thriftstore.service.ThriftStoreService;

@Tag(name = "ThriftStoreController", description = "서울시 개인서비스 요금 정보를 저장하는 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/thrift")
public class ThriftStoreController {

    private final ThriftStoreService thriftStoreService;


    @GetMapping("/connectAPI/{startIndex}/{endIndex}")
    public void connectOpenAPI(@PathVariable("startIndex") String startIndex,
        @PathVariable("endIndex") String endIndex) throws IOException {

        String json = thriftStoreService.connectOpenAPI(startIndex, endIndex);
        thriftStoreService.processAndStoreData(json);
    }
}
