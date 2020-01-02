package ru.zagamaza.sublearn.subtitles.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Season {
    private Integer season;
    private List<Episode> episodes;

}
