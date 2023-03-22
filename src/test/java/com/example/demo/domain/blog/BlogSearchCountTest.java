package com.example.demo.domain.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BlogSearchCountTest {

    @Autowired
    private BlogSearchCountRepository blogSearchCountRepository;


    @Test
    void save_shouldThrowConstraintViolationException_whenQueryIsNull() {
        // given
        BlogSearchCount blogSearchCount = new BlogSearchCount();
        blogSearchCount.setCount(1L);

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> blogSearchCountRepository.save(blogSearchCount));
    }

    @Test
    void save_shouldThrowDataIntegrityViolationException_whenQueryIsNotUnique() {
        // given
        BlogSearchCount blogSearchCount1 = new BlogSearchCount();
        blogSearchCount1.setQuery("spring boot");
        blogSearchCount1.setCount(1L);

        BlogSearchCount blogSearchCount2 = new BlogSearchCount();
        blogSearchCount2.setQuery("spring boot");
        blogSearchCount2.setCount(2L);

        // when
        blogSearchCountRepository.save(blogSearchCount1);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> blogSearchCountRepository.save(blogSearchCount2));
    }

    @Test
    void increaseCount_shouldIncreaseCountByOne() {
        // given
        BlogSearchCount blogSearchCount = new BlogSearchCount();
        blogSearchCount.setCount(1L);

        // when
        blogSearchCount.increaseCount();

        // then
        assertThat(blogSearchCount.getCount()).isEqualTo(2L);
    }
}
