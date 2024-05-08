package zzandori.zzanmoa.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "market_place")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "market_id")
    private String marketId;

    @Column(name = "market_name")
    private String marketName;

    @Column(name = "market_address")
    private String marketAddress;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Builder
    public MarketPlace(String marketId, String marketName, String marketAddress, Double latitude, Double longitude) {
        this.marketId = marketId;
        this.marketName = marketName;
        this.marketAddress = marketAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
