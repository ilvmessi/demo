package com.example.demo.dto.blog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
public class BlogListResponseDto {

    @Schema(description = "블로그 글 제목")
    private String title;

    @Schema(description = "블로그 글 요약")
    private String contents;

    @Schema(description = "블로그 글 URL")
    private String url;

    @Schema(description = "블로그의 이름")
    private String blogname;

    @Schema(description = "검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음")
    private String thumbnail;

    @Schema(description = "블로그 글 작성시간")
    private String datetime;

}
