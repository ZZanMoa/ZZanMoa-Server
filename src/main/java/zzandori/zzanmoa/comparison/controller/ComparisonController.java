package zzandori.zzanmoa.comparison.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzandori.zzanmoa.comparison.dto.ComparisonRequestDto;
import zzandori.zzanmoa.comparison.dto.ComparisonResponseDto;
import zzandori.zzanmoa.comparison.service.ComparisonService;

@Tag(name = "ComparisonController", description = "시장 가격 비교 응답 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/market")
public class ComparisonController {

    private final ComparisonService comparisonService;

    @PostMapping("/compare")
    public ResponseEntity<ComparisonResponseDto> comparePrice(@RequestBody ComparisonRequestDto comparisonRequestDto) {
        return ResponseEntity.ok(comparisonService.comparePrice(comparisonRequestDto));
    }

}
