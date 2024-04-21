package zzandori.zzanmoa.thriftstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemInfoDTO {
    private String itemId;
    private String item;
    private String category;
    private Integer price;
}
