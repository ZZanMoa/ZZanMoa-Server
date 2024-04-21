package zzandori.zzanmoa.grocery.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.grocery.service.GroceryService;

@Tag(name = "GroceryController", description = "식료품 정보를 다루는 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("grocery")
public class GroceryController {

    private final GroceryService shoppingCartService;

    @GetMapping("/save")
    public void saveGroceries() {
        shoppingCartService.saveGroceries();
    }

}
