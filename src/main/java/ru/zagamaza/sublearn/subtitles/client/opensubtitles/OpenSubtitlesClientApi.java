package ru.zagamaza.sublearn.subtitles.client.opensubtitles;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "open-subtitle", url = "https://rest.opensubtitles.org/")
@RequestMapping(value = "/search")
public interface OpenSubtitlesClientApi {

    @GetMapping("/imdbid-{imdbId}")
    List<SubtitleInfo> searchByImdb(
            @RequestHeader("User-Agent") String agent,
            @PathVariable("imdbId") String imdbId
    );

    @GetMapping("/imdbid-{imdbId}/season-{season}/episode-{episode}/sublanguageid-{language}")
    List<SubtitleInfo> searchByImdb(
            @RequestHeader("User-Agent") String agent,
            @PathVariable("imdbId") String imdbId,
            @PathVariable("season") Integer season,
            @PathVariable("episode") Integer episode,
            @PathVariable("language") OpenSuLang language
    );

    @GetMapping("/query-{name}")
    List<SubtitleInfo> searchByName(
            @RequestHeader("User-Agent") String agent,
            @PathVariable("name") String name
    );

    @GetMapping("/query-{name}/season-{season}/episode-{episode}/sublanguageid-{language}")
    List<SubtitleInfo> searchByName(
            @RequestHeader("User-Agent") String agent,
            @PathVariable("name") String name,
            @PathVariable("season") Integer season,
            @PathVariable("episode") Integer episode,
            @PathVariable("language") OpenSuLang language
    );

}
