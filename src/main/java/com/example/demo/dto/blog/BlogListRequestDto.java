package com.example.demo.dto.blog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class BlogListRequestDto {

    private String query;
    private String sort;
    private int page;
    private int limit;

}
