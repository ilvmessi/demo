package com.example.demo.controller.blog;

import com.example.demo.domain.blog.BlogService;
import com.example.demo.domain.exception.ErrorCode;
import com.example.demo.dto.blog.BlogListRequestDto;
import com.example.demo.dto.blog.BlogListResponseDto;
import com.example.demo.dto.blog.BlogPopularListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Blog", description = "블로그 관련 API")
@RestController
@RequestMapping("/api/v1/blog/")
@RequiredArgsConstructor
@Validated
public class BlogController {

    private final BlogService blogService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorCode.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(summary = "블로그 검색", description = "블로그 검색 API 입니다.")
    @GetMapping("list")
    public ResponseEntity<List<BlogListResponseDto>> getBlogList(
            @NotBlank(message = "필수값 입니다.")
            @Parameter(description = "검색어", example = "messi", required = true)
            @RequestParam(name = "query") String query,

            @Parameter(description = "정렬 기준", example = "accuracy")
            @Pattern(regexp = "^(accuracy|recency)$", message = "정렬 기준은 accuracy 또는 recency만 가능합니다.")
            @RequestParam(name = "sort", defaultValue = "accuracy", required = false) String sort,

            @Max(value = 50, message = "페이지 번호는 50 이하여야 합니다.")
            @Parameter(description = "페이지 번호", example = "1")
            @Positive(message = "페이지 번호는 양수만 가능합니다.")
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,

            @Max(value = 50, message = "페이지 크기는 50 이하여야 합니다.")
            @Parameter(description = "페이지 크기", example = "10")
            @Positive(message = "페이지 크기는 양수만 가능합니다.")
            @RequestParam(name = "limit", defaultValue = "10", required = false) int limit
    ) {
        BlogListRequestDto requestDto = BlogListRequestDto.builder()
                .query(query)
                .sort(sort)
                .page(page)
                .limit(limit).build();


        List<BlogListResponseDto> blogDtos = blogService.searchBlogs(requestDto);

        return ResponseEntity.ok(blogDtos);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @Operation(summary = "인기 검색어 목록", description = "인기 검색어 목록 API 입니다.")
    @GetMapping("popular/list")
    public ResponseEntity<List<BlogPopularListResponseDto>> getBlogPopularList() {

        List<BlogPopularListResponseDto> blogDtos = blogService.searchPopularBlogs();

        return ResponseEntity.ok(blogDtos);
    }
}
