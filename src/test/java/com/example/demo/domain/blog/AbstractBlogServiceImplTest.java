package com.example.demo.domain.blog;

import com.example.demo.dto.blog.BlogListRequestDto;
import com.example.demo.dto.blog.BlogListResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class AbstractBlogServiceImplTest {

    @Test
    void increaseSearchCount_should_create_new_entity_if_query_does_not_exist() {
        // given
        String query = "test";
        BlogSearchCountRepository mockRepository = mock(BlogSearchCountRepository.class);
        when(mockRepository.findByQuery(query)).thenReturn(Optional.empty());

        AbstractBlogServiceImpl blogService = new AbstractBlogServiceImpl(mockRepository) {
            @Override
            public List<BlogListResponseDto> searchBlogs(BlogListRequestDto requestDto) {
                return null;
            }
        };

        // when
        blogService.increaseSearchCount(query);

        // then
        ArgumentCaptor<BlogSearchCount> captor = ArgumentCaptor.forClass(BlogSearchCount.class);
        verify(mockRepository).save(captor.capture());
        BlogSearchCount savedCount = captor.getValue();
        assertEquals(query, savedCount.getQuery());
        assertEquals(1L, savedCount.getCount());
    }

    @Test
    void increaseSearchCount_should_increase_count_if_query_exists() {
        // given
        String query = "test";
        BlogSearchCount count = new BlogSearchCount();
        count.setQuery(query);
        count.setCount(1L);
        BlogSearchCountRepository mockRepository = mock(BlogSearchCountRepository.class);
        when(mockRepository.findByQuery(query)).thenReturn(Optional.of(count));

        AbstractBlogServiceImpl blogService = new AbstractBlogServiceImpl(mockRepository) {
            @Override
            public List<BlogListResponseDto> searchBlogs(BlogListRequestDto requestDto) {
                return null;
            }
        };

        // when
        blogService.increaseSearchCount(query);

        // then
        ArgumentCaptor<BlogSearchCount> captor = ArgumentCaptor.forClass(BlogSearchCount.class);
        verify(mockRepository).save(captor.capture());
        BlogSearchCount savedCount = captor.getValue();
        assertEquals(query, savedCount.getQuery());
        assertEquals(2L, savedCount.getCount());
    }
}

