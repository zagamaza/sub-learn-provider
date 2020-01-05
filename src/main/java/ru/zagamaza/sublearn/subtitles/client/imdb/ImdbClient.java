package ru.zagamaza.sublearn.subtitles.client.imdb;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zagamaza.sublearn.subtitles.client.imdb.dto.Search;
import ru.zagamaza.sublearn.subtitles.client.imdb.dto.SearchWrapper;

@FeignClient(name = "imdb", url = "http://www.omdbapi.com/")
public interface ImdbClient {

    @GetMapping
    SearchWrapper search(
            @RequestParam(value = "apiKey") String apiKey,
            @RequestParam("s") String query,
            @RequestParam("type") String type
    );

    @GetMapping
    Search getByImdb(
            @RequestParam(value = "apiKey") String apiKey,
            @RequestParam("i") String imdbId
    );


}
