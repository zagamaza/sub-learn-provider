package ru.zagamaza.sublearn.subtitles.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.zagamaza.sublearn.subtitles.client.opensubtitles.OpenSubtitlesClient;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.CollectionClientApi;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.EpisodeClientApi;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionRequest;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.EpisodeDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.EpisodeRequest;
import ru.zagamaza.sublearn.subtitles.dto.Episode;
import ru.zagamaza.sublearn.subtitles.dto.FoundCollection;
import ru.zagamaza.sublearn.subtitles.dto.Season;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipInputStream;

import static ru.zagamaza.sublearn.subtitles.util.DtoUtils.toCollectionRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubtitlesUploadService {

    public static final String DELIMITER = "-";

    @Value("${sublearn.back.url}")
    private String sublearnBackUrl;

    private final RestTemplate restTemplate;
    private final CollectionClientApi collectionClientApi;
    private final EpisodeClientApi episodeClientApi;
    private final NotificationService notificationService;
    private final OpenSubtitlesClient openSubtitlesClient;

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000, multiplier = 2))
    public void uploadWithSendStatus(FoundCollection imdbMovie, List<Season> seasons, Long userId) {
        if (seasons.isEmpty()) {
            notificationService.sendFailNotification(imdbMovie, userId);
            return;
        }
        try {
            upload(imdbMovie, seasons, userId);
        } catch (Exception e) {
            log.error("upload failed", e);
            notificationService.sendFailNotification(imdbMovie, userId);
            return;
        }
        notificationService.sendSuccessNotification(imdbMovie, userId);
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000, multiplier = 2))
    public void upload(FoundCollection imdbMovie, List<Season> seasons, Long userId){
        CollectionDto collectionDto = collectionClientApi.getByImdbId(imdbMovie.getImdbID());
        if (collectionDto == null) {
            CollectionRequest collectionRequest = toCollectionRequest(imdbMovie);
            collectionRequest.setUserId(userId);
            collectionDto = collectionClientApi.create(collectionRequest);
        }

        for (Season season : seasons) {
            for (Episode episode : season.getEpisodes()) {
                if (!episodeExists(collectionDto, season, episode)) {
                    File sub = openSubtitlesClient.getSubtitleFileByUrl(episode.getSubtitleUrl());
                    EpisodeDto episodeDto = createEpisode(collectionDto, season, episode);
                    try {
                        uploadFileAndGetEpisode(episodeDto.getId(), sub);
                    } catch (Exception e){
                        log.error(collectionDto.getName() + " " + episodeDto.getSeason() + "-" + episodeDto.getEpisode(), e);
                        episodeClientApi.delete(episodeDto.getId());
                        return;
                    }
                }
            }
        }
        updateStatusCollection(collectionDto, imdbMovie, userId);
    }


    private void updateStatusCollection(CollectionDto collectionDto, FoundCollection imdbMovie, Long userId) {
        CollectionRequest collectionRequest = toCollectionRequest(collectionDto);
        collectionRequest.setFinished(calculateFinished(imdbMovie));
        collectionRequest.setUserId(userId);
        collectionClientApi.update(collectionRequest);
    }

    private EpisodeDto createEpisode(CollectionDto collectionDto, Season season, Episode episode) {
        return episodeClientApi.create(
                EpisodeRequest.builder()
                              .collectionId(collectionDto.getId())
                              .season(season.getSeason())
                              .episode(episode.getEpisode())
                              .build()
        );
    }

    private boolean episodeExists(CollectionDto collectionDto, Season season, Episode episode) {
        EpisodeDto episodeDto = episodeClientApi.getByCollectionIdAndSeasonAndSeries(
                collectionDto.getId(),
                season.getSeason(),
                episode.getEpisode()
        );
        return episodeDto != null;
    }

    private boolean calculateFinished(FoundCollection imdbMovie) {
        String[] years = imdbMovie.getYear().split(DELIMITER);
        return years.length == 2;
    }

    private EpisodeDto uploadFileAndGetEpisode(Long episodeID, File file) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(file));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        ResponseEntity<EpisodeDto> response = restTemplate.exchange(
                sublearnBackUrl + "/episodes/" + episodeID,
                HttpMethod.PUT,
                new HttpEntity(map, headers),
                EpisodeDto.class
        );
        return response.getBody();
    }

}
