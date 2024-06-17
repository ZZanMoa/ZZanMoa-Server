package zzandori.zzanmoa.marketplace.entity;

import jakarta.persistence.CascadeType;
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

@Table(name = "market_place_google_ids")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketPlaceGoogleIds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id")
    private String placeId;

    @OneToOne
    @JoinColumn(name = "market_id", referencedColumnName = "market_id")
    private MarketPlace marketPlace;

    @Builder
    public MarketPlaceGoogleIds(String placeId, MarketPlace marketPlace) {
        this.placeId = placeId;
        this.marketPlace = marketPlace;
    }
}
