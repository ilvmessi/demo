package com.example.demo.domain.blog;

import com.example.demo.dto.blog.BlogPopularListResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class AbstractBlogServiceImpl implements BlogService {

    private final BlogSearchCountRepository blogSearchCountRepository;

    public AbstractBlogServiceImpl(BlogSearchCountRepository blogSearchCountRepository) {
        this.blogSearchCountRepository = blogSearchCountRepository;
    }

    public List<BlogPopularListResponseDto> searchPopularBlogs() {
        List<BlogSearchCount> blogSearchCounts = blogSearchCountRepository.findTop10ByOrderByCountDesc();
        return blogSearchCounts.stream()
                .map(blogSearchCount -> BlogPopularListResponseDto.builder()
                        .query(blogSearchCount.getQuery())
                        .count(blogSearchCount.getCount())
                        .build())
                .toList();
    }

    @Transactional
    public void increaseSearchCount(String query) {
        Optional<BlogSearchCount> blogSearchCountOptional = blogSearchCountRepository.findByQuery(query);

        BlogSearchCount blogSearchCount;
        if (blogSearchCountOptional.isPresent()) {
            blogSearchCount = blogSearchCountOptional.get();
            blogSearchCount.increaseCount();
        } else {
            blogSearchCount = new BlogSearchCount();
            blogSearchCount.setQuery(query);
            blogSearchCount.setCount(1L);
        }
        blogSearchCountRepository.save(blogSearchCount);
    }
}
