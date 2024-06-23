package zzandori.zzanmoa.chatgptapi.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zzandori.zzanmoa.chatgptapi.dto.ChatGPTRequest;
import zzandori.zzanmoa.chatgptapi.dto.ChatGPTResponse;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    @Value("${OPENAI_GPT_MODEL}")
    private String model;

    @Value("${OPENAI_GPT_URL}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public String getChatResponse(List<String> reviews){
        String combinedReviews = reviews.stream().collect(Collectors.joining("\n"));
        String prompt = "다음 리뷰를 분석하고 구조화된 요약을 제공해 주세요. 먼저 해당 요약은 다음 섹션을 포함해야 하며 각 섹션의 제목은 볼드체를 사용해주세요. "
            + "극단적으로 부정적인 키워드는 순화해서 작성하세요. 분석할 수 없는 항목은 생략하여 제공해주세요:\n"
            + "\n"
            + "종합 평가\n"
            + "긍정적 키워드\n"
            + "부정적 키워드\n"
            + "맛\n"
            + "서비스\n"
            + "가격\n"
            + "분위기\n"
            + "\n"
            + "여기 리뷰들이 있습니다:" + combinedReviews;


        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse chatGPTResponse =  restTemplate.postForObject(url, request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
