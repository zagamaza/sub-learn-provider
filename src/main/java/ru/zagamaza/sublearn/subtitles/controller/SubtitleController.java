package ru.zagamaza.sublearn.subtitles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zagamaza.sublearn.subtitles.dto.FoundCollection;
import ru.zagamaza.sublearn.subtitles.dto.Season;
import ru.zagamaza.sublearn.subtitles.service.ImdbSearchService;
import ru.zagamaza.sublearn.subtitles.service.SubtitlesService;
import ru.zagamaza.sublearn.subtitles.service.SubtitlesUploadService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubtitleController {

    private final ImdbSearchService imdbSearchService;
    private final SubtitlesService subtitlesService;
    private final SubtitlesUploadService subtitlesUploadService;

    @GetMapping("/collections/find")
    public List<FoundCollection> findCollections(@RequestParam String title) {
        return imdbSearchService.findCollection(title);
    }

    @GetMapping("/collections/{imdbId}/subtitles")
    public List<Season> getSubtitles(@PathVariable String imdbId) {
        return subtitlesService.getSubtitles(imdbId);
    }

    @Async
    @PostMapping("/collections/{imdbId}/subtitles/upload")
    public void upload(@PathVariable String imdbId, @RequestParam Long userId) {
        FoundCollection foundCollection = imdbSearchService.findCollectionByImdbId(imdbId);
        List<Season> subtitles = getSubtitles(imdbId);
        subtitlesUploadService.upload(foundCollection, subtitles, userId);
    }

}
