package com.example.demo.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    BLOG_ENGINE_API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Blog engine api server error"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
