package com.example.demo.dto.blog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BlogPopularListResponseDto {

    @Schema(description = "검색어")
    private String query;

    @Schema(description = "조회수")
    private Long count;
}
