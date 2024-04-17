package zzandori.zzanmoa.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

}
