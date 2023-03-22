package com.example.demo.domain.blog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaverBlogWebClientFactory {

    private final String baseUrl;
    private final String id;
    private final String secret;

    public NaverBlogWebClientFactory(@Value("${api.naver.url}") String baseUrl,
                                     @Value("${api.naver.id}") String id,
                                     @Value("${api.naver.secret}") String secret
    ) {
        this.baseUrl = baseUrl;
        this.id = id;
        this.secret = secret;
    }

    public WebClient create() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Naver-Client-Id", id)
                .defaultHeader("X-Naver-Client-Secret", secret)
                .build();
    }
}
