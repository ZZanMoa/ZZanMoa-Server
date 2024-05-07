package zzandori.zzanmoa.latlng.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "latlng_district")
public class LatLngDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String district;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LatLngSubDistrict> subDistricts = new ArrayList<>();

    public void addSubDistrict(final LatLngSubDistrict subDistrict) {
        subDistricts.add(subDistrict);
    }

    @Builder
    public LatLngDistrict(String district) {
        this.district = district;
    }
}
