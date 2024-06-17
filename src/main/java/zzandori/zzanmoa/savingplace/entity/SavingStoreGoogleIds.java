package zzandori.zzanmoa.savingplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;

@Entity(name = "saving_store_google_ids")
public class SavingStoreGoogleIds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_Id")
    private String placeId;

    @OneToOne
    @JoinColumn(name = "saving_store_id", referencedColumnName = "store_id")
    private SavingStore savingStore;

    @Builder
    public SavingStoreGoogleIds(String placeId, SavingStore savingStore) {
        this.placeId = placeId;
        this.savingStore = savingStore;
    }
}
