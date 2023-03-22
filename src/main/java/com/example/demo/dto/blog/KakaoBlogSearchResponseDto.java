package com.example.demo.dto.blog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoBlogSearchResponseDto {

    private KakaoBlogSearchMetaDto meta;
    private List<KakaoBlogSearchDocumentDto> documents;

    @Getter
    @Setter
    public static class KakaoBlogSearchMetaDto {

        private int total_count;
        private int pageable_count;
        private boolean is_end;

    }

    @Getter
    @Setter
    public static class KakaoBlogSearchDocumentDto {

        private String title;
        private String contents;
        private String url;
        private String blogname;
        private String thumbnail;
        private String datetime;

    }
}
