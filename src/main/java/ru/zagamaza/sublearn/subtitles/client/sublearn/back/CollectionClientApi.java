package ru.zagamaza.sublearn.subtitles.client.sublearn.back;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionCondensedDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionRequest;

import java.util.List;

@FeignClient(contextId = "collections", name = "collections", url = "${sublearn.back.url}")
public interface CollectionClientApi {

    @GetMapping("/collections/{id}")
    CollectionDto get(@PathVariable("id") Long id);

    @GetMapping("/collections/imdb/{imdbId}")
    CollectionDto getByImdbId(@PathVariable String imdbId);

    @GetMapping("/collections/serials/finished")
    List<CollectionCondensedDto> findNotFinishedSerials();

    @GetMapping("/collections/{id}/users/{userId}/copy")
    CollectionDto copy(@PathVariable("id") Long id, @PathVariable("userId") Long userId);

    @PutMapping("/collections/{id}")
    CollectionDto updateIsSerial(@PathVariable("id") Long id, @RequestParam("isSerial") Boolean isSerial);

    @PutMapping("/collections")
    CollectionDto update(@RequestBody CollectionRequest collectionRequest);

    @PostMapping("/collections")
    CollectionDto create(@RequestBody CollectionRequest collectionRequest);

    @DeleteMapping("/collections/{id}")
    void delete(@PathVariable("id") Long id);

    @DeleteMapping("/collections/{id}/users/{userId}")
    void deleteLinkUserToCollection(@PathVariable("id") Long id, @PathVariable("userId") Long userId);

}
