package zzandori.zzanmoa.latlng.service;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzandori.zzanmoa.latlng.dto.LatLngDistrictDto;
import zzandori.zzanmoa.latlng.dto.LatLngSubDistrictDto;
import zzandori.zzanmoa.latlng.entity.LatLngDistrict;
import zzandori.zzanmoa.latlng.entity.LatLngSubDistrict;
import zzandori.zzanmoa.latlng.repository.LatLngDistrictRepository;
import zzandori.zzanmoa.latlng.repository.LatLngSubDistrictRepository;

@Service
@RequiredArgsConstructor
public class LatLngService {

    private final LatLngDistrictRepository latLngDistrictRepository;
    private final LatLngSubDistrictRepository latLngSubDistrictRepository;

    public List<LatLngDistrictDto> getLatitudeAndLongitude() {
        return latLngDistrictRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private LatLngDistrictDto convertToDTO(LatLngDistrict district) {
        return LatLngDistrictDto.builder()
            .district(district.getDistrict())
            .subDistricts(district.getSubDistricts().stream()
                .map(this::convertSubDistrictToDTO)
                .collect(Collectors.toList()))
            .build();
    }

    private LatLngSubDistrictDto convertSubDistrictToDTO(LatLngSubDistrict subDistrict) {
        return LatLngSubDistrictDto.builder()
            .dong(subDistrict.getDong())
            .latitude(subDistrict.getLatitude())
            .longitude(subDistrict.getLongitude())
            .build();
    }

    @Transactional
    public void saveLatitudeAndLongitude() {
        String filePath = "src/main/resources/seoul_latitude_and_longitude.csv";
        Map<String, List<LatLngSubDistrict>> districts = parseCsvFile(filePath);
        persistData(districts);
    }

    private Map<String, List<LatLngSubDistrict>> parseCsvFile(String filePath) {
        Map<String, List<LatLngSubDistrict>> districts = new HashMap<>();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath),
            StandardCharsets.UTF_8);
            CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                String districtName = csvRecord.get(2);
                LatLngSubDistrict subDistrict = LatLngSubDistrict.builder()
                    .dong(csvRecord.get(3))
                    .latitude(Double.parseDouble(csvRecord.get(4)))
                    .longitude(Double.parseDouble(csvRecord.get(5)))
                    .build();
                districts.computeIfAbsent(districtName, k -> new ArrayList<>()).add(subDistrict);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return districts;
    }

    private void persistData(Map<String, List<LatLngSubDistrict>> districts) {
        districts.forEach((districtName, subDistricts) -> {
            LatLngDistrict district = LatLngDistrict.builder().district(districtName).build();
            latLngDistrictRepository.save(district);

            subDistricts.forEach(subDistrictData -> {
                LatLngSubDistrict subDistrict = LatLngSubDistrict.builder()
                    .district(district)
                    .dong(subDistrictData.getDong())
                    .latitude(subDistrictData.getLatitude())
                    .longitude(subDistrictData.getLongitude())
                    .build();
                latLngSubDistrictRepository.save(subDistrict);
            });
        });
    }

}
