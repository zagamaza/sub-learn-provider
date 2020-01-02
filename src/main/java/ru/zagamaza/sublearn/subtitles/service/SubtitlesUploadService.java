package ru.zagamaza.sublearn.subtitles.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.CollectionClientApi;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.EpisodeClientApi;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionDto;
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

@Service
@RequiredArgsConstructor
public class SubtitlesUploadService {

    @Value("${sublearn.back.url}")
    private String sublearnBackUrl;

    private final RestTemplate restTemplate;
    private final CollectionClientApi collectionClientApi;
    private final EpisodeClientApi episodeClientApi;

    public void upload(FoundCollection imdbMovie, List<Season> seasons) {

        CollectionDto collectionDto = collectionClientApi.getByImdbId(imdbMovie.getImdbID());
        if (collectionDto == null)
            collectionDto = collectionClientApi.create(toCollectionRequest(imdbMovie));

        for (Season season : seasons) {
            for (Episode episode : season.getEpisodes()) {
                EpisodeDto episodeDto = episodeClientApi.create(EpisodeRequest.builder()
                        .collectionId(collectionDto.getId())
                        .season(season.getSeason())
                        .episode(episode.getEpisode())
                        .build());
                File sub = getSubtitleFileByUrl(episode.getSubtitleUrl());
                uploadFileAndGetEpisode(episodeDto.getId(), sub);
            }
        }
    }


    @SneakyThrows
    private File getSubtitleFileByUrl(String subtitleUrl) {
        File sub = File.createTempFile("sub", "");

        try (ZipInputStream inputStream = new ZipInputStream(new URL(subtitleUrl).openStream());
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
