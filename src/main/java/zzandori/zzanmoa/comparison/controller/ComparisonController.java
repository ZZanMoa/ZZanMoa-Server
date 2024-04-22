package zzandori.zzanmoa.comparison.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.comparison.dto.ComparisonRequestDto;
import zzandori.zzanmoa.comparison.dto.ComparisonResponseDto;
import zzandori.zzanmoa.comparison.service.ComparisonService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/compare")
public class ComparisonController {

    private final ComparisonService comparisonService;

    @PostMapping
    public ResponseEntity<ComparisonResponseDto> comparePrice(@RequestBody ComparisonRequestDto comparisonRequestDto) {
        return ResponseEntity.ok(comparisonService.comparePrice(comparisonRequestDto));
    }

}
