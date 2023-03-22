package com.example.demo.domain.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KakaoBlogWebClientFactoryTest {

    @Autowired
    private KakaoBlogWebClientFactory factory;

    @Test
    void create_should_return_web_client_with_authorization_header() {
        assertThat(factory.create()).isNotNull();
    }
}
