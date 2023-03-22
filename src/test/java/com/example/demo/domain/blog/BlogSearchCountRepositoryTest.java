package com.example.demo.domain.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BlogSearchCountRepositoryTest {

    @Autowired
    private BlogSearchCountRepository blogSearchCountRepository;

    @Test
    void findByQuery_shouldReturnBlogSearchCount_whenQueryExists() {
        // given
        String query = "test";
        BlogSearchCount expected = new BlogSearchCount();
        expected.setQuery(query);
        expected.setCount(5L);

        blogSearchCountRepository.save(expected);

        // when
        Optional<BlogSearchCount> actualOptional = blogSearchCountRepository.findByQuery(query);

        // then
        assertTrue(actualOptional.isPresent());
        BlogSearchCount actual = actualOptional.get();
        assertEquals(expected.getQuery(), actual.getQuery());
        assertEquals(expected.getCount(), actual.getCount());
    }

    @Test
    void findTop10ByOrderByCountDesc_shouldReturnTop10BlogSearchCounts() {
        // given
        List<BlogSearchCount> expected = new ArrayList<>();
        for (Long i = 0L; i < 15; i++) {
            BlogSearchCount blogSearchCount = new BlogSearchCount();
            blogSearchCount.setQuery("query" + i);
            blogSearchCount.setCount(i);
            blogSearchCountRepository.save(blogSearchCount);
            expected.add(blogSearchCount);
        }

        // when
        List<BlogSearchCount> actual = blogSearchCountRepository.findTop10ByOrderByCountDesc();

        // then
        assertEquals(10, actual.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(expected.get(14 - i), actual.get(i));
        }
    }


}
