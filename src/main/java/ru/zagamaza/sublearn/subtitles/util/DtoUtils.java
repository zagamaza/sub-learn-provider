package ru.zagamaza.sublearn.subtitles.util;

import ru.zagamaza.sublearn.subtitles.client.imdb.dto.Search;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionRequest;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.Lang;
import ru.zagamaza.sublearn.subtitles.dto.FoundCollection;

import java.time.LocalDateTime;

public class DtoUtils {

    public static FoundCollection toFoundCollection(Search search) {
        return FoundCollection.builder()
                .imdbID(search.getImdbID())
                .title(search.getTitle())
                .poster(search.getPoster())
                .type(search.getType())
                .year(search.getYear())
                .build();
    }


    public static CollectionRequest toCollectionRequest(FoundCollection imdbMovie) {
        return CollectionRequest.builder()
                .imdbId(imdbMovie.getImdbID())
                .isSerial(true)
                .isShared(true)
                .name(imdbMovie.getTitle())
                .url(imdbMovie.getPoster())
                .userId(2L)
                .created(LocalDateTime.now())
                .rating(0)
                .lang(Lang.EN_RU)
                .build();
    }
}
