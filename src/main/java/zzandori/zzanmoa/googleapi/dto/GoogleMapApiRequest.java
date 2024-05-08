package zzandori.zzanmoa.googleapi.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleMapApiRequest {

    @Value("${GOOGLE_MAP_API_BASE_URL}")
    private String GOOGLE_MAP_API_BASE_URL;
    @Value("${GOOGLE_MAP_API_KEY}")
    private String GOOGLE_MAP_API_KEY;

    public String requestUrl(String address) throws UnsupportedEncodingException {
        address = address.replace("?", "");
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        StringBuilder urlBuilder = new StringBuilder(GOOGLE_MAP_API_BASE_URL)
            .append("?address=").append(encodedAddress)
            .append("&key=").append(GOOGLE_MAP_API_KEY);
        System.out.println("->" + urlBuilder.toString());
        return urlBuilder.toString();
    }
}
