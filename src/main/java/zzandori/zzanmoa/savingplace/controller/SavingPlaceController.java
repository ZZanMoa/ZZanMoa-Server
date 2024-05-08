package zzandori.zzanmoa.savingplace.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.UnsupportedEncodingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.savingplace.service.SavingPlaceService;
import zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO;
import zzandori.zzanmoa.savingplace.dto.StoreInfoDTO;

@Tag(name = "SavingPlaceController", description = "알뜰가게 정보 응답 컨트롤러")

@RestController
@RequiredArgsConstructor
@RequestMapping("/saving-place")
public class SavingPlaceController {

    private final SavingPlaceService savingPlaceService;

    @GetMapping("/save")
    public void save() throws InterruptedException, UnsupportedEncodingException {
        savingPlaceService.save();
    }

    @GetMapping("/get/category")
    public List<CategoryPriceDTO> getCategoryPrice(){
        return savingPlaceService.getCategoryPrice();
    }

    @GetMapping("/get/store")
    public List<StoreInfoDTO> getStores(){
        return savingPlaceService.getAllStoresWithItems();
    }

}
