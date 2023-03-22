package com.example.demo.domain.blog;

import com.example.demo.domain.exception.RestApiException;
import com.example.demo.dto.blog.BlogListRequestDto;
import com.example.demo.dto.blog.BlogListResponseDto;
import com.example.demo.dto.blog.KakaoBlogSearchResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class KakaoBlogServiceImplTest {

    @Test
    void searchBlogs_should_return_blog_list() {
        // given
        final String query = "test";
        final String sort = "accuracy";
        final int limit = 10;
        final int page = 1;

        BlogListRequestDto requestDto = BlogListRequestDto.builder()
                .query(query)
                .sort(sort)
                .page(page)
                .limit(limit).build();

        KakaoBlogSearchResponseDto responseBody = new KakaoBlogSearchResponseDto();
        KakaoBlogSearchResponseDto.KakaoBlogSearchDocumentDto document = new KakaoBlogSearchResponseDto.KakaoBlogSearchDocumentDto();
        document.setTitle("Test Blog Title");
        document.setContents("Test Blog Contents");
        document.setUrl("https://testblog.com");
        document.setBlogname("Test Blog Name");
        document.setThumbnail("https://testblog.com/thumbnail.jpg");
        document.setDatetime("2022-03-21T13:00:00.000+09:00");

        KakaoBlogSearchResponseDto.KakaoBlogSearchMetaDto meta = new KakaoBlogSearchResponseDto.KakaoBlogSearchMetaDto();
        meta.setTotal_count(1);
        meta.set_end(true);
        meta.setPageable_count(1);

        responseBody.setDocuments(Collections.singletonList(document));

        ResponseEntity<KakaoBlogSearchResponseDto> mockResponseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        BlogSearchCountRepository mockRepository = mock(BlogSearchCountRepository.class);

        // mock web client
        WebClient mockWebClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);

        when(mockWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(any(Function.class))).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.toEntity(eq(KakaoBlogSearchResponseDto.class))).thenReturn(Mono.just(mockResponseEntity));

        // mock web client factory
        KakaoBlogWebClientFactory mockWebClientFactory = mock(KakaoBlogWebClientFactory.class);
        when(mockWebClientFactory.create()).thenReturn(mockWebClient);

        // create blog service
        KakaoBlogServiceImpl blogService = new KakaoBlogServiceImpl(mockWebClientFactory, mockRepository);

        // when
        List<BlogListResponseDto> result = blogService.searchBlogs(requestDto);

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo(document.getTitle());
        assertThat(result.get(0).getContents()).isEqualTo(document.getContents());
        assertThat(result.get(0).getUrl()).isEqualTo(document.getUrl());
        assertThat(result.get(0).getBlogname()).isEqualTo(document.getBlogname());
        assertThat(result.get(0).getThumbnail()).isEqualTo(document.getThumbnail());

        ArgumentCaptor<BlogSearchCount> captor = ArgumentCaptor.forClass(BlogSearchCount.class);
        verify(mockRepository).save(captor.capture());
        BlogSearchCount savedCount = captor.getValue();
        assertEquals(query, savedCount.getQuery());
        assertEquals(1L, savedCount.getCount());
    }

    @Test
    void searchBlogs_should_handle_internal_server_error() {
        // given
        final String query = "test";
        final String sort = "accuracy";
        final int limit = 10;
        final int page = 1;

        BlogListRequestDto requestDto = BlogListRequestDto.builder()
                .query(query)
                .sort(sort)
                .page(page)
                .limit(limit).build();


        // mock web client
        WebClient mockWebClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);
        when(mockWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(any(Function.class))).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.toEntity(eq(KakaoBlogSearchResponseDto.class)))
                .thenReturn(Mono.error(new WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, null, null)));

        // mock web client factory
        KakaoBlogWebClientFactory mockWebClientFactory = mock(KakaoBlogWebClientFactory.class);
        when(mockWebClientFactory.create()).thenReturn(mockWebClient);

        BlogSearchCountRepository mockRepository = mock(BlogSearchCountRepository.class);

        // create blog service
        KakaoBlogServiceImpl blogService = new KakaoBlogServiceImpl(mockWebClientFactory, mockRepository);

        // when, then
        assertThrows(WebClientResponseException.class, () -> blogService.searchBlogs(requestDto));
    }


    @Test
    void searchBlogs_should_handle_rest_api_server_error() {
        // given
        final String query = "test";
        final String sort = "accuracy";
        final int limit = 10;
        final int page = 1;

        BlogListRequestDto requestDto = BlogListRequestDto.builder()
                .query(query)
                .sort(sort)
                .page(page)
                .limit(limit).build();

        KakaoBlogSearchResponseDto responseBody = new KakaoBlogSearchResponseDto();
        ResponseEntity<KakaoBlogSearchResponseDto> mockResponseEntity = new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        BlogSearchCountRepository mockRepository = mock(BlogSearchCountRepository.class);

        // mock web client
        WebClient mockWebClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec mockUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);
        when(mockWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(any(Function.class))).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.toEntity(eq(KakaoBlogSearchResponseDto.class))).thenReturn(Mono.just(mockResponseEntity));

        // mock web client factory
        KakaoBlogWebClientFactory mockWebClientFactory = mock(KakaoBlogWebClientFactory.class);
        when(mockWebClientFactory.create()).thenReturn(mockWebClient);

        // create blog service
        KakaoBlogServiceImpl blogService = new KakaoBlogServiceImpl(mockWebClientFactory, mockRepository);

        // when, then
        assertThrows(RestApiException.class, () -> blogService.searchBlogs(requestDto));
    }
}
