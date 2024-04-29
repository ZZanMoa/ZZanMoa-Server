package zzandori.zzanmoa.bargain.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.bargain.entity.Bargain;
import zzandori.zzanmoa.openapi.utility.JsonUtil;

@Service
@RequiredArgsConstructor
public class BargainDataProcessor {

    private final JsonUtil jsonUtil;

    public List<Bargain> parseJsonToStoreList(String json) throws IOException {
        JsonNode rootNode = jsonUtil.parseJson(json);
        JsonNode rows = rootNode.path("VwNews").path("row");
        List<Bargain> bargainList = new ArrayList<>();

        if (rows.isArray()) {
            for (JsonNode row : rows) {
                bargainList.add(buildBargain(row));
            }
        }
        return bargainList;
    }

    private Bargain buildBargain(JsonNode row) {
        return Bargain.builder()
            .districtId(Integer.parseInt(row.get("N_GU_CODE").asText()))
            .districtName(row.get("N_GU_NAME").asText())
            .eventId(convertToInt(row.get("N_EVENT_CODE").asText()))
            .eventName(row.get("N_EVENT_NAME").asText())
            .title(decodeHtmlEntities(row.get("N_TITLE").asText()))
            .content(decodeHtmlEntities(row.get("N_CONTENTS").asText()))
            .views(convertToInt(row.get("N_VIEW_COUNT").asText()))
            .createdAt(parseToTimestamp(row.get("REG_DATE").asText()))
            .build();
    }


    private String decodeHtmlEntities(String input) {
        return StringEscapeUtils.unescapeHtml4(input);
    }

    private Timestamp parseToTimestamp(String dateTimeString) {
        dateTimeString = dateTimeString.replace(".0", "");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

        return Timestamp.valueOf(localDateTime);
    }

    private int convertToInt(String input) {
        String numberStr = input.replaceAll("\"0", "");
        numberStr = numberStr.replaceAll("\\..*", "");

        return Integer.parseInt(numberStr);
    }
}
