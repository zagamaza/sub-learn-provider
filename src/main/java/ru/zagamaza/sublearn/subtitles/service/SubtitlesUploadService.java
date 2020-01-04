package ru.zagamaza.sublearn.subtitles.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.CollectionClientApi;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.EpisodeClientApi;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.NotificationClient;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionRequest;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.EpisodeDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.EpisodeRequest;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.NotificationDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.NotificationType;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.UserDto;
import ru.zagamaza.sublearn.subtitles.dto.Episode;
import ru.zagamaza.sublearn.subtitles.dto.FoundCollection;
import ru.zagamaza.sublearn.subtitles.dto.Season;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipInputStream;

import static ru.zagamaza.sublearn.subtitles.util.DtoUtils.toCollectionRequest;

@Service
@RequiredArgsConstructor
public class SubtitlesUploadService {

    private static final String COLLECTION_UPLOAD = "🎉 Коллекция *%s* успешно загружена\nМожете найти ее в своих коллекциях.";
    private static final String COLLECTION_FAIL = "😓 К сожалению, мы не смогли загрузить коллекцию *%s*.";

    @Value("${sublearn.back.url}")
    private String sublearnBackUrl;

    private final RestTemplate restTemplate;
    private final CollectionClientApi collectionClientApi;
    private final EpisodeClientApi episodeClientApi;
    private final NotificationClient notificationClient;

    public void upload(FoundCollection imdbMovie, List<Season> seasons, Long userId) {
        if (seasons.isEmpty()) {
            sendFailNotification(imdbMovie, userId);
            return;
        }
        CollectionDto collectionDto = collectionClientApi.getByImdbId(imdbMovie.getImdbID());
        if (collectionDto == null) {
            CollectionRequest collectionRequest = toCollectionRequest(imdbMovie);
            collectionRequest.setUserId(userId);
            collectionDto = collectionClientApi.create(collectionRequest);
        }

        try {
            for (Season season : seasons) {
                for (Episode episode : season.getEpisodes()) {
                    if (!episodeExists(collectionDto, season, episode)) {
                        File sub = getSubtitleFileByUrl(episode.getSubtitleUrl());
                        EpisodeDto episodeDto = createEpisode(collectionDto, season, episode);
                        uploadFileAndGetEpisode(episodeDto.getId(), sub);
                    }
                }
            }
        } catch (Exception e) {
            sendFailNotification(imdbMovie, userId);
            return;
        }
        sendSuccessNotification(collectionDto, userId);
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

    private void sendSuccessNotification(CollectionDto collectionDto, Long userId) {
        NotificationDto notification = NotificationDto.builder()
                                                      .notificationType(NotificationType.MESSAGE)
                                                      .userDto(UserDto.builder().id(userId).build())
                                                      .text(String.format(COLLECTION_UPLOAD, collectionDto.getName()))
                                                      .build();
        notificationClient.create(notification);
    }

    private void sendFailNotification(FoundCollection foundCollection, Long userId) {
        NotificationDto notification = NotificationDto.builder()
                                                      .notificationType(NotificationType.MESSAGE)
                                                      .userDto(UserDto.builder().id(userId).build())
                                                      .text(String.format(COLLECTION_FAIL, foundCollection.getTitle()))
                                                      .build();
        notificationClient.create(notification);
    }


    @SneakyThrows
    @Retryable(
            value = {Exception.class},
            maxAttempts = 40,
            backoff = @Backoff(delay = 30000))
    private File getSubtitleFileByUrl(String subtitleUrl) {
        File sub = File.createTempFile("sub", "");

        try (
                ZipInputStream inputStream = new ZipInputStream(new URL(subtitleUrl).openStream());
                FileOutputStream fileOS = new FileOutputStream(sub)
        ) {
            inputStream.getNextEntry();
            inputStream.transferTo(fileOS);
        }
        return sub;
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
