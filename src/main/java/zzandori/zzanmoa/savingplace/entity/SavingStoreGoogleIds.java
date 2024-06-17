package zzandori.zzanmoa.savingplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Table(name = "saving_store_google_ids")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavingStoreGoogleIds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_Id")
    private String placeId;

    @OneToOne
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private SavingStore savingStore;

    @Builder
    public SavingStoreGoogleIds(String placeId, SavingStore savingStore) {
        this.placeId = placeId;
        this.savingStore = savingStore;
    }
}
