package com.example.demo.domain.blog;

import com.example.demo.dto.blog.BlogListRequestDto;
import com.example.demo.dto.blog.BlogListResponseDto;
import com.example.demo.dto.blog.BlogPopularListResponseDto;

import java.util.List;

public interface BlogService {
    List<BlogListResponseDto> searchBlogs(BlogListRequestDto requestDto);

    void increaseSearchCount(String query);

    List<BlogPopularListResponseDto> searchPopularBlogs();

}
