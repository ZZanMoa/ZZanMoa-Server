package zzandori.zzanmoa.openapi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.openapi.config.OpenAPIConfig;

@RequiredArgsConstructor
@Service
public class OpenAPIService {

    private final OpenAPIConfig openAPIConfig;

    // open api 요청시 필수 요청 인자 설정
    public StringBuilder initRequest(String service, String startIndex, String endIndex)
        throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(openAPIConfig.getKey(), "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(service, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(startIndex, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(endIndex, "UTF-8"));

        return urlBuilder;
    }

    // API 연결 및 데이터 수집
    public String connectOpenAPI(StringBuilder urlBuilder) throws IOException {
        HttpURLConnection conn = createConnection(urlBuilder.toString());
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd = getReader(conn);
        String result = readResponse(rd);
        rd.close();
        conn.disconnect();
        return result;
    }


    // HttpURLConnection 설정 및 생성
    private HttpURLConnection createConnection(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        return conn;
    }

    // 응답 코드 처리 및 BufferedReader 설정
    private BufferedReader getReader(HttpURLConnection conn) throws IOException {
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            return new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            return new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
    }

    // 응답 데이터 읽기
    private String readResponse(BufferedReader reader) throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }


}
