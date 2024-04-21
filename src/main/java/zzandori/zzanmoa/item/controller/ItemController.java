package zzandori.zzanmoa.item.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.item.service.ItemService;

@Tag(name = "ItemController", description = "식료품 정보를 다루는 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/save")
    public void saveGroceries() {
        itemService.saveItems();
    }

}
