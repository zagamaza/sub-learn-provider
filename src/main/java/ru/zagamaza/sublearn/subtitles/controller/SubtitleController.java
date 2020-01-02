package ru.zagamaza.sublearn.subtitles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/collections/{imdbId}/subtitles/upload")
    public void upload(@PathVariable String imdbId) {
        subtitlesUploadService.upload(
                imdbSearchService.findCollectionByImdbId(imdbId),
                getSubtitles(imdbId));
    }

}
