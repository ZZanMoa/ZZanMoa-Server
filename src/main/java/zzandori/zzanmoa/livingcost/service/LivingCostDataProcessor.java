package zzandori.zzanmoa.livingcost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.livingcost.entity.LivingCost;
import zzandori.zzanmoa.openapi.utility.JsonUtil;

@Service
@RequiredArgsConstructor
public class LivingCostDataProcessor {

    private final JsonUtil jsonUtil;

    public List<LivingCost> parseJsonToLivingCostList(String json) throws JsonProcessingException {
        JsonNode rootNode = jsonUtil.parseJson(json);
        JsonNode rows = rootNode.path("VwNotice").path("row");
        List<LivingCost> livingCostList = new ArrayList<>();

        if (rows.isArray()) {
            for (JsonNode row : rows) {
                livingCostList.add(buildLivingCost(row));
            }
        }

        return livingCostList;
    }

    private LivingCost buildLivingCost(JsonNode row) {
        return LivingCost.builder()
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
