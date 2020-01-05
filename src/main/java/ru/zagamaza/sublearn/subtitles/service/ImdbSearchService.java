package ru.zagamaza.sublearn.subtitles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.zagamaza.sublearn.subtitles.client.imdb.ImdbClient;
import ru.zagamaza.sublearn.subtitles.dto.FoundCollection;
import ru.zagamaza.sublearn.subtitles.util.DtoUtils;

import java.util.List;
import java.util.stream.Collectors;

import static ru.zagamaza.sublearn.subtitles.util.DtoUtils.toFoundCollection;

@Service
@RequiredArgsConstructor
public class ImdbSearchService {

    private final ImdbClient imdbClient;

    @Value("${imdb.api.key}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private String apiKey;


    public List<FoundCollection> findCollection(String title) {
        return imdbClient.search(apiKey, title, "series")
                         .getSearch()
                         .stream()
                         .map(DtoUtils::toFoundCollection)
                         .collect(Collectors.toList());
    }

    public FoundCollection findCollectionByImdbId(String imdbId) {
        return toFoundCollection(imdbClient.getByImdb(apiKey, imdbId));
    }

}
