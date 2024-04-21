package zzandori.zzanmoa.thriftstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StoreInfoDTO {
    private String storeId;
    private String storeName;
    private String phoneNumber;
    private String address;
    private List<ItemInfoDTO> items;

}
