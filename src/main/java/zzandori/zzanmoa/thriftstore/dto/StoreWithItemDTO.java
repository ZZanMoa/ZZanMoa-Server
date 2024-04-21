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
public class StoreWithItemDTO {

    private String storeId;
    private String storeName;
    private String category;
    private String itemId;
    private String phoneNumber;
    private String address;
}
