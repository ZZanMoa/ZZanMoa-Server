package zzandori.zzanmoa.thriftstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "thrift_store", uniqueConstraints = @UniqueConstraint(columnNames = {"storeId", "itemId"}))
public class ThriftStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "category")
    private String category;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;
}
