package zzandori.zzanmoa.googleapi.dto.review;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import zzandori.zzanmoa.googleapi.exception.GoogleApiErrorCode;
import zzandori.zzanmoa.googleapi.exception.GoogleApiException;

@Configuration
public class ReviewRequest {

    @Value("${GOOGLE_MAP_API_REVIEW_BASE_URL}")
    private String GOOGLE_MAP_API_REVIEW_BASE_URL;
    @Value("${GOOGLE_MAP_API_KEY}")
    private String GOOGLE_MAP_API_KEY;

    public String requestUrl(String placeId) {
        String encodedPlaceId = null;
        try {
            encodedPlaceId = URLEncoder.encode(placeId, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new GoogleApiException(GoogleApiErrorCode.UNSUPPORTED_ENCODING);
        }
        StringBuilder urlBuilder = new StringBuilder(GOOGLE_MAP_API_REVIEW_BASE_URL)
            .append("?fields=").append("reviews")
            .append("&language=").append("ko")
            .append("&place_id=").append(encodedPlaceId)
            .append("&key=").append(GOOGLE_MAP_API_KEY);
        return urlBuilder.toString();
    }

}
