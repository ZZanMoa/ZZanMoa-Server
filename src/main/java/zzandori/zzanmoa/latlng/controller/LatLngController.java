package zzandori.zzanmoa.latlng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.latlng.dto.LatLngResponse;
import zzandori.zzanmoa.latlng.service.LatLngService;

@RestController
@RequestMapping("/latlng")
@RequiredArgsConstructor
public class LatLngController {

    private final LatLngService latLngService;

    @GetMapping("/save")
    public void saveLatitudeAndLongitude() {
        latLngService.saveLatitudeAndLongitude();
    }

    @GetMapping("/get")
    public LatLngResponse getLatitudeAndLongitude() {
        return latLngService.getLatitudeAndLongitude();
    }

}
