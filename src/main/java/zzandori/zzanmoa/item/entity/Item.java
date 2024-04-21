package zzandori.zzanmoa.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_name")
    private String itemName;

    public Item() {

    }

    @Builder
    public Item(String itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }
}
