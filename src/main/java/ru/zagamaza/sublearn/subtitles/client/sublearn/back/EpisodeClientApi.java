package ru.zagamaza.sublearn.subtitles.client.sublearn.back;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.EpisodeDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.EpisodeRequest;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.RestPageImpl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@FeignClient(contextId = "episodes", name = "episodes", url = "${sublearn.back.url}")
public interface EpisodeClientApi {

    @GetMapping("/episodes/{id}")
    EpisodeDto get(@PathVariable("id") Long id);

    @GetMapping("/episodes/{id}/users/{userId}")
    Integer getLearnedPercent(@PathVariable("id") Long id, @PathVariable("userId") Long userId);

    @GetMapping("/episodes/collections/{collectionId}/seasons")
    List<Integer> getSeasonsByCollectionId(@PathVariable("collectionId") Long collectionId);

    @GetMapping("/episodes/collections/{collectionId}/season")
    RestPageImpl<EpisodeDto> getByCollectionIdAndSeason(
            @PathVariable("collectionId") Long collectionId,
            @RequestParam("season") Integer season
    );

    @GetMapping("/episodes/collections/{collectionId}/season/series")
    EpisodeDto getByCollectionIdAndSeasonAndSeries(
            @PathVariable("collectionId") Long collectionId,
            @RequestParam("season") Integer season,
            @RequestParam("series") Integer series
    );

    @PostMapping("/episodes")
    EpisodeDto create(@RequestBody EpisodeRequest episodeRequest);

    @PutMapping("/episodes/{id}")
    EpisodeDto addWordsInEpisode(@PathVariable("id") Long id, @RequestParam("file") MultipartFile submissions);

    @PutMapping("/episodes")
    EpisodeDto update(@RequestBody EpisodeRequest episodeRequest);

    @DeleteMapping("/episodes/{id}")
    void delete(@PathVariable("id") Long id);

}
