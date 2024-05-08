package zzandori.zzanmoa.googleapi.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import zzandori.zzanmoa.googleapi.dto.GoogleMapApiRequest;
import zzandori.zzanmoa.googleapi.dto.GoogleMapApiResponse;
import zzandori.zzanmoa.googleapi.dto.Location;
import zzandori.zzanmoa.webclient.util.WebClientUtil;

@Service
@RequiredArgsConstructor
public class GoogleMapApiService {

    private final WebClientUtil webClientUtil;
    private final GoogleMapApiRequest googleMapApiRequest;

    public Mono<String> requestTest(String address) throws UnsupportedEncodingException {
        String requestUrl = googleMapApiRequest.requestUrl(address);
        return webClientUtil.get(
            requestUrl,
            String.class);
    }


    public Location request(String address) throws UnsupportedEncodingException {
        String requestUrl = googleMapApiRequest.requestUrl(address);
        Mono<GoogleMapApiResponse> responseMono = webClientUtil.get(
            requestUrl,
            GoogleMapApiResponse.class);

        return responseMono.flatMap(dto -> {
            return Mono.justOrEmpty(Optional.ofNullable(dto.getResults())
                .filter(results -> !results.isEmpty())
                .map(results -> results.get(0).getGeometry().getLocation()));
        }).block();
    }

}
