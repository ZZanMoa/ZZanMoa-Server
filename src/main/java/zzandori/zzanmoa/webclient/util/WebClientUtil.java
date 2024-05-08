package zzandori.zzanmoa.webclient.util;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import zzandori.zzanmoa.webclient.config.WebClientConfig;

@Component
@RequiredArgsConstructor
public class WebClientUtil {

    private final WebClientConfig webClientConfig;

    public <T> Mono<T> get(String uri, Class<T> responseDtoClass) {
        return webClientConfig.webClient()
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(responseDtoClass);
    }

    public <T, V> Mono<T> post(String uri, V requestDto, Class<T> responseDtoClass) {
        return webClientConfig.webClient()
            .post()
            .uri(uri)
            .bodyValue(requestDto)
            .retrieve()
            .bodyToMono(responseDtoClass);
    }

}
