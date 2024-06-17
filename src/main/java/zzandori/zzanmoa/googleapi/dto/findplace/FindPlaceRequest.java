package zzandori.zzanmoa.googleapi.dto.findplace;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import zzandori.zzanmoa.googleapi.exception.GoogleApiErrorCode;
import zzandori.zzanmoa.googleapi.exception.GoogleApiException;

@Configuration
public class FindPlaceRequest {

    @Value("${GOOGLE_MAP_API_FINDPLACE_BASE_URL}")
    private String GOOGLE_MAP_API_FINDPLACE_BASE_URL;
    @Value("${GOOGLE_MAP_API_KEY}")
    private String GOOGLE_MAP_API_KEY;

    public String requestUrl(String address) {
        String encodedAddress = null;
        try {
            encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new GoogleApiException(GoogleApiErrorCode.UNSUPPORTED_ENCODING);
        }
        StringBuilder urlBuilder = new StringBuilder(GOOGLE_MAP_API_FINDPLACE_BASE_URL)
            .append("?input=").append(encodedAddress)
            .append("&inputtype=").append("textquery")
            .append("&fields=").append("formatted_address").append("%2C").append("place_id")
            .append("&key=").append(GOOGLE_MAP_API_KEY);
        return urlBuilder.toString();
    }

}
