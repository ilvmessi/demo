package com.example.demo.dto.blog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NaverBlogSearchResponseDto {

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NaverBlogSearchItemDto> items;

    @Getter
    @Setter
    public static class NaverBlogSearchItemDto {
        private String title;
        private String link;
        private String description;
        private String bloggername;
        private String bloggerlink;
        private String postdate;
    }

}
