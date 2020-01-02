package ru.zagamaza.sublearn.subtitles.client.imdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchWrapper {

    @JsonProperty("Search")
    List<Search> search;
}
