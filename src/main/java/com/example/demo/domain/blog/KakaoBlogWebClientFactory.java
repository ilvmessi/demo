package com.example.demo.domain.blog;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoBlogWebClientFactory {

    private final String baseUrl;

    private final String restApiKey;

    public KakaoBlogWebClientFactory(@Value("${api.kakao.url}") String baseUrl,
                                     @Value("${api.kakao.key}") String restApiKey) {
        this.baseUrl = baseUrl;
        this.restApiKey = restApiKey;
    }

    public WebClient create() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + restApiKey)
                .build();
    }
}
