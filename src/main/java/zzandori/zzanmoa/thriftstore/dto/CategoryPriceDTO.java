package zzandori.zzanmoa.thriftstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CategoryPriceDTO {
    private String category;
    private Integer minPrice;
    private Integer maxPrice;

}
