package zzandori.zzanmoa.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;

@Table(name = "market_place", uniqueConstraints = @UniqueConstraint(columnNames = {"marketId"}))
@Getter
@Entity
public class MarketPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "market_id")
    private String marketId;

    @Column(name = "market_name")
    private String marketName;

    public MarketPlace() {

    }

    @Builder
    public MarketPlace(String marketId, String marketName) {
        this.marketId = marketId;
        this.marketName = marketName;
    }
}
