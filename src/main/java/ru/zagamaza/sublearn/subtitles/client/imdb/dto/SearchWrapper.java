package ru.zagamaza.sublearn.subtitles.client.imdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Data
public class SearchWrapper {

    @JsonProperty("Search")
    List<Search> search;

    public List<Search> getSearch() {
        if (CollectionUtils.isEmpty(search)) {
            return Collections.emptyList();
        } else { return search; }
    }

}
