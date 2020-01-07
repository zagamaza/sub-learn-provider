package ru.zagamaza.sublearn.subtitles.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.zagamaza.sublearn.subtitles.client.opensubtitles.OpenSubtitlesClient;
import ru.zagamaza.sublearn.subtitles.client.opensubtitles.SubtitleInfo;

import java.util.List;

//@SpringBootTest
class OpenSubtitlesClientTest {

    @Autowired
    OpenSubtitlesClient openSubtitlesClient;

//    @Test
    public void testClient() {
        List<SubtitleInfo> big_bang = openSubtitlesClient.searchByName("The Lord of the Rings");

        System.out.println(big_bang);

    }
//    @Test
    public void testClientImdbId() {
        List<SubtitleInfo> big_bang = openSubtitlesClient.searchByImdb("tt0167260");

        System.out.println(big_bang);

    }

}