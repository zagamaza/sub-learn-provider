package ru.zagamaza.sublearn.subtitles.client.opensubtitles;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipInputStream;

@Component
@RequiredArgsConstructor
public class OpenSubtitlesClient {

    @Value("${opensubtitles.useragent:}")
    private String[] opensubtitlesUseragents;

    private final OpenSubtitlesClientApi client;

    public List<SubtitleInfo> searchByImdb(String imdbId) {
        return client.searchByImdb(getRandomAgent(), imdbId);
    }

    @Cacheable(value = "subtitle", key = "{#imdbId, #season, #episode, #language}", unless = "#result.isEmpty()")
    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000, multiplier = 2))
    public List<SubtitleInfo> searchByImdb(String imdbId, Integer season, Integer episode, OpenSuLang language) {
        return client.searchByImdb(getRandomAgent(), imdbId, season, episode, language);
    }

    public List<SubtitleInfo> searchByName(String name) {
        return client.searchByName(getRandomAgent(), name);
    }

    @Cacheable(value = "subtitle", key = "{#name, #season, #episode, #language}")
    @Retryable(
            value = Exception.class,
            maxAttempts = 10,
            backoff = @Backoff(delay = 30000, multiplier = 2))
    public List<SubtitleInfo> searchByName(String name, Integer season, Integer episode, OpenSuLang language) {
        return client.searchByName(getRandomAgent(), name, season, episode, language);
    }

    @SneakyThrows
    @Retryable(
            value = Exception.class,
            maxAttempts = 10,
            backoff = @Backoff(delay = 30000, multiplier = 3))
    public File getSubtitleFileByUrl(String subtitleUrl) {
        File sub = File.createTempFile("sub", "");

        try (
                ZipInputStream inputStream = new ZipInputStream(new URL(subtitleUrl).openStream());
                FileOutputStream fileOS = new FileOutputStream(sub)
        ) {
            inputStream.getNextEntry();
            inputStream.transferTo(fileOS);
        }

        FileSystemResource fileSystemResource = new FileSystemResource(sub);
        String text = IOUtils.toString(fileSystemResource.getInputStream());
        if (StringUtils.isEmpty(text)){
            throw new RuntimeException("File empty");
        }
        return sub;
    }

    private String getRandomAgent() {
        int randomNumber = (int)(Math.random() * (opensubtitlesUseragents.length));
        return opensubtitlesUseragents[randomNumber];
    }

}
