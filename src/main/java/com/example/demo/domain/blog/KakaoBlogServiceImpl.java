package com.example.demo.domain.blog;

import com.example.demo.domain.exception.CommonErrorCode;
import com.example.demo.domain.exception.RestApiException;
import com.example.demo.dto.blog.BlogListResponseDto;
import com.example.demo.dto.blog.BlogListRequestDto;
import com.example.demo.dto.blog.KakaoBlogSearchResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "blog", name = "service.type", havingValue = "kakao")
public class KakaoBlogServiceImpl extends AbstractBlogServiceImpl {

    private final WebClient webClient;

    public KakaoBlogServiceImpl(KakaoBlogWebClientFactory webClientFactory, BlogSearchCountRepository blogSearchCountRepository) {
        super(blogSearchCountRepository);
        this.webClient = webClientFactory.create();
    }

    @Override
    @Transactional
    public List<BlogListResponseDto> searchBlogs(BlogListRequestDto requestDto) {
        String query = requestDto.getQuery();
        String sort = requestDto.getSort();
        int limit = requestDto.getLimit();
        int page = requestDto.getPage();

        ResponseEntity<KakaoBlogSearchResponseDto> responseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/search/blog")
                        .queryParam("query", query)
                        .queryParam("size", limit)
                        .queryParam("page", page)
                        .queryParam("sort", sort)
                        .build())
                .retrieve()
                .toEntity(KakaoBlogSearchResponseDto.class)
                .block();


        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
            KakaoBlogSearchResponseDto responseBody = responseEntity.getBody();
            if (responseBody != null) {
                // 검색어에 대한 조회수 증가
                increaseSearchCount(query);
                return responseBody.getDocuments().stream()
                        .map(document -> BlogListResponseDto.builder()
                                .title(document.getTitle())
                                .contents(document.getContents())
                                .url(document.getUrl())
                                .blogname(document.getBlogname())
                                .thumbnail(document.getThumbnail())
                                .datetime(document.getDatetime())
                                .build())
                        .toList();
            }
        }

        throw new RestApiException(CommonErrorCode.BLOG_ENGINE_API_SERVER_ERROR);
    }
}
