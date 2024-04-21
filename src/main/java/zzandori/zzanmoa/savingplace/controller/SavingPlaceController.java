package zzandori.zzanmoa.savingplace.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.savingplace.service.SavingPlaceService;
import zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO;
import zzandori.zzanmoa.savingplace.dto.StoreInfoDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/saving-place")
public class SavingPlaceController {

    private final SavingPlaceService savingPlaceService;

    @GetMapping("/get/category")
    public List<CategoryPriceDTO> getCategoryPrice(){
        return savingPlaceService.getCategoryPrice();
    }

    @GetMapping("get/store")
    public List<StoreInfoDTO> getStores(){
        return savingPlaceService.getAllStoresWithItems();
    }

}
