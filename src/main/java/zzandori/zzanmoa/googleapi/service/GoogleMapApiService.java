package zzandori.zzanmoa.googleapi.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import zzandori.zzanmoa.googleapi.dto.findplace.FindPlaceRequest;
import zzandori.zzanmoa.googleapi.dto.findplace.FindPlaceResponse;
import zzandori.zzanmoa.googleapi.dto.findplace.Candidate;
import zzandori.zzanmoa.googleapi.dto.geometry.GeocodeRequest;
import zzandori.zzanmoa.googleapi.dto.geometry.GeocodeResponse;
import zzandori.zzanmoa.googleapi.dto.geometry.Location;
import zzandori.zzanmoa.googleapi.dto.review.Review;
import zzandori.zzanmoa.googleapi.dto.review.ReviewRequest;
import zzandori.zzanmoa.googleapi.dto.review.ReviewResponse;
import zzandori.zzanmoa.webclient.util.WebClientUtil;

@Service
@RequiredArgsConstructor
public class GoogleMapApiService {

    private final WebClientUtil webClientUtil;
    private final GeocodeRequest geocodeRequest;
    private final FindPlaceRequest findPlaceRequest;
    private final ReviewRequest reviewRequest;

    

    public GeocodeResponse requestGeocode(String address) {
        String requestUrl = geocodeRequest.requestUrl(address);
        Mono<GeocodeResponse> responseMono = webClientUtil.get(
            requestUrl,
            GeocodeResponse.class);

        return responseMono.block();
    }

    public FindPlaceResponse requestFindPlace(String address){
        String requestUrl = findPlaceRequest.requestUrl(address);
        Mono<FindPlaceResponse> responseMono = webClientUtil.get(
            requestUrl,
            FindPlaceResponse.class
        );

        return responseMono.block();
    }

    public ReviewResponse requestReview(String placeId) {
        String requestUrl = reviewRequest.requestUrl(placeId);
        Mono<ReviewResponse> responseMono = webClientUtil.get(
            requestUrl,
            ReviewResponse.class
        );

        return responseMono.block();
    }

}
