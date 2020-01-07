package ru.zagamaza.sublearn.subtitles.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Episode {
    private Integer episode;
    private String subtitleUrl;
}
