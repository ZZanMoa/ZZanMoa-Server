package zzandori.zzanmoa.latlng.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "latlng_subdistrict")
public class LatLngSubDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district")
    private LatLngDistrict district;

    private String dong;

    private Double latitude;

    private Double longitude;

    @Builder
    public LatLngSubDistrict(LatLngDistrict district, String dong, Double latitude,
        Double longitude) {
        this.district = district;
        this.dong = dong;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
