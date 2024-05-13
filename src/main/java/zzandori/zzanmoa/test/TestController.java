package zzandori.zzanmoa.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TestController", description = "테스트를 위한 컨트롤러")
@RequiredArgsConstructor
@RestController
public class TestController {

    @GetMapping("/")
    public String test() {
        return "Hello, World!";
    }

    @GetMapping("/zzanmoa")
    public String test2() {
        return "Hello, zzandori!";
    }

    @GetMapping("/zzagmoa")
    public ResponseEntity<String> test3() {
        throw new TestException(TestErrorCode.INVALID_PARAMETER);
    }

}
