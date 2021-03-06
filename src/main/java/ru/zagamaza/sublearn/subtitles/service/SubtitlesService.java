package ru.zagamaza.sublearn.subtitles.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.zagamaza.sublearn.subtitles.client.opensubtitles.OpenSubtitlesClient;
import ru.zagamaza.sublearn.subtitles.client.opensubtitles.SubtitleInfo;
import ru.zagamaza.sublearn.subtitles.dto.Episode;
import ru.zagamaza.sublearn.subtitles.dto.Season;

import java.util.ArrayList;
import java.util.List;

import static ru.zagamaza.sublearn.subtitles.client.opensubtitles.OpenSuLang.eng;


@Service
@RequiredArgsConstructor
@Slf4j
public class SubtitlesService {

    private final OpenSubtitlesClient openSubtitlesClient;

    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 30000, multiplier = 2))
    public List<Season> getSubtitles(String imdbId) {
        return recursiveSearchSeasons(imdbId, 1);
    }

    private List<Season> recursiveSearchSeasons(String imdbId, Integer currentSeason) {
        System.out.println("se - " + currentSeason);
        List<Episode> episodes = recursiveSearchEpisodes(imdbId, currentSeason, 1);
        if (episodes.isEmpty()) {
            return new ArrayList<>();
        }

        List<Season> seasons = recursiveSearchSeasons(imdbId, currentSeason + 1);
        seasons.add(Season.builder()
                .season(currentSeason)
                .episodes(episodes)
                .build());
        return seasons;
    }

    private List<Episode> recursiveSearchEpisodes(String imdbId, Integer currentSeason, Integer currentEpisode) {
        System.out.println("ep - " + currentEpisode);
        List<SubtitleInfo> subtitleInfos = openSubtitlesClient.searchByImdb(imdbId, currentSeason, currentEpisode, eng);
        if (subtitleInfos.isEmpty()) {
            return new ArrayList<>();
        }
        List<Episode> episodes = recursiveSearchEpisodes(imdbId, currentSeason, currentEpisode + 1);
        SubtitleInfo subtitleInfo = subtitleInfos.get(0);
        episodes.add(
                Episode.builder()
                        .episode(Integer.parseInt(subtitleInfo.getSeriesEpisode()))
                        .subtitleUrl(subtitleInfo.getZipDownloadLink())
                        .build());
        return episodes;
    }

}
