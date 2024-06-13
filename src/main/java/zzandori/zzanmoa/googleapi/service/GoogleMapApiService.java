package zzandori.zzanmoa.googleapi.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import zzandori.zzanmoa.googleapi.dto.FindPlaceRequest;
import zzandori.zzanmoa.googleapi.dto.FindPlaceResponse;
import zzandori.zzanmoa.googleapi.dto.Candidate;
import zzandori.zzanmoa.googleapi.dto.GeocodeRequest;
import zzandori.zzanmoa.googleapi.dto.GeocodeResponse;
import zzandori.zzanmoa.googleapi.dto.Location;
import zzandori.zzanmoa.webclient.util.WebClientUtil;

@Service
@RequiredArgsConstructor
public class GoogleMapApiService {

    private final WebClientUtil webClientUtil;
    private final GeocodeRequest geocodeRequest;
    private final FindPlaceRequest findPlaceRequest;

    public Location requestGeocode(String address) throws UnsupportedEncodingException {
        String requestUrl = geocodeRequest.requestUrl(address);
        Mono<GeocodeResponse> responseMono = webClientUtil.get(
            requestUrl,
            GeocodeResponse.class);

        return responseMono.flatMap(dto -> {
            return Mono.justOrEmpty(Optional.ofNullable(dto.getResults())
                .filter(results -> !results.isEmpty())
                .map(results -> results.get(0).getGeometry().getLocation()));
        }).block();
    }

    public Candidate requestFindPlace(String address) throws UnsupportedEncodingException {
        String requestUrl = findPlaceRequest.requestUrl(address);
        Mono<FindPlaceResponse> responseMono = webClientUtil.get(
            requestUrl,
            FindPlaceResponse.class
        );

        return responseMono.flatMap(dto -> {
            return Mono.justOrEmpty(Optional.ofNullable(dto.getCandidates())
                .filter(candidates -> !candidates.isEmpty())
                .map(candidates -> candidates.get(0)));
        }).block();
    }

}
