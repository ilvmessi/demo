package com.example.demo.domain.blog;

import com.example.demo.domain.exception.CommonErrorCode;
import com.example.demo.domain.exception.RestApiException;
import com.example.demo.dto.blog.BlogListRequestDto;
import com.example.demo.dto.blog.BlogListResponseDto;
import com.example.demo.dto.blog.NaverBlogSearchResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "blog", name = "service.type", havingValue = "naver")
public class NaverBlogServiceImpl extends AbstractBlogServiceImpl {

    private final WebClient webClient;

    public NaverBlogServiceImpl(NaverBlogWebClientFactory webClientFactory,
                                BlogSearchCountRepository blogSearchCountRepository) {
        super(blogSearchCountRepository);
        this.webClient = webClientFactory.create();
    }

    @Override
    @Transactional
    public List<BlogListResponseDto> searchBlogs(BlogListRequestDto requestDto) {
        String query = requestDto.getQuery();
        String sort = requestDto.getSort().equals("recency") ? "date" : "sim";
        int size = requestDto.getLimit();
        int offset = (requestDto.getPage() - 1) * requestDto.getLimit() + 1;

        ResponseEntity<NaverBlogSearchResponseDto> responseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/blog.json")
                        .queryParam("query", query)
                        .queryParam("display", size)
                        .queryParam("start", offset)
                        .queryParam("sort", sort)
                        .build())
                .retrieve()
                .toEntity(NaverBlogSearchResponseDto.class)
                .block();

        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
            NaverBlogSearchResponseDto responseBody = responseEntity.getBody();
            if (responseBody != null) {
                // 검색어에 대한 조회수 증가
                increaseSearchCount(query);
                return responseBody.getItems().stream()
                        .map(item -> BlogListResponseDto.builder()
                                .title(item.getTitle())
                                .contents(item.getDescription())
                                .url(item.getLink())
                                .blogname(item.getBloggername())
                                .datetime(item.getPostdate())
                                .build())
                        .toList();
            }
        }

        throw new RestApiException(CommonErrorCode.BLOG_ENGINE_API_SERVER_ERROR);
    }
}
