package zzandori.zzanmoa.latlng.service;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.latlng.dto.LatLngResponse;
import zzandori.zzanmoa.latlng.entity.LatLngDistrict;
import zzandori.zzanmoa.latlng.entity.LatLngSubDistrict;
import zzandori.zzanmoa.latlng.repository.LatLngDistrictRepository;
import zzandori.zzanmoa.latlng.repository.LatLngSubDistrictRepository;

@Service
@RequiredArgsConstructor
public class LatLngService {

    private final LatLngDistrictRepository latLngDistrictRepository;
    private final LatLngSubDistrictRepository latLngSubDistrictRepository;

    public LatLngResponse getLatitudeAndLongitude() {
        return null;
    }

    public void saveLatitudeAndLongitude() {
        String filePath = "src/main/resources/seoul_latitude_and_longitude.csv";

        Map<String, List<LatLngSubDistrict>> districts = new HashMap<>();

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath),
            StandardCharsets.UTF_8);
            CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                String district = csvRecord.get(2);
                String dong = csvRecord.get(3);
                Double latitude = Double.valueOf(csvRecord.get(4));
                Double longitude = Double.valueOf(csvRecord.get(5));

                if (!districts.containsKey(district)) {
                    districts.put(district, new ArrayList<>());
                } else {
                    districts.get(district).add(LatLngSubDistrict.builder()
                        .dong(dong)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String district : districts.keySet()) {
            LatLngDistrict latLngDistrict = LatLngDistrict.builder()
                .district(district)
                .build();
            latLngDistrictRepository.save(latLngDistrict);

            List<LatLngSubDistrict> subDistricts = districts.get(district);
            for (LatLngSubDistrict subDistrict : subDistricts) {
                LatLngSubDistrict latLngSubDistrict = LatLngSubDistrict.builder()
                    .district(latLngDistrict)
                    .dong(subDistrict.getDong())
                    .latitude(subDistrict.getLatitude())
                    .longitude(subDistrict.getLongitude())
                    .build();

                latLngDistrict.addSubDistrict(latLngSubDistrict);

                latLngSubDistrictRepository.save(latLngSubDistrict);
            }

        }
    }

}
