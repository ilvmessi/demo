package com.example.demo.domain.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();

    HttpStatus getHttpStatus();

    String getMessage();

    @Getter
    @Builder
    @RequiredArgsConstructor
    class ErrorResponse {

        private final String code;
        private final String message;
    }
}
