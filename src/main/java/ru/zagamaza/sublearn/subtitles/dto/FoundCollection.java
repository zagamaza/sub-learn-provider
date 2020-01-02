package ru.zagamaza.sublearn.subtitles.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoundCollection {
    @JsonProperty("title")
    private String title;
    @JsonProperty("year")
    private String year;
    @JsonProperty("imdbID")
    private String imdbID;
    @JsonProperty("type")
    private String type;
    @JsonProperty("poster")
    private String poster;
}
