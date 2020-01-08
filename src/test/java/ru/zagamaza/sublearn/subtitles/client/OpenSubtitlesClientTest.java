package ru.zagamaza.sublearn.subtitles.client;

import org.springframework.beans.factory.annotation.Autowired;
import ru.zagamaza.sublearn.subtitles.client.opensubtitles.OpenSubtitlesClient;
import ru.zagamaza.sublearn.subtitles.client.opensubtitles.SubtitleInfo;

import java.util.List;

//@SpringBootTest
class OpenSubtitlesClientTest {

    @Autowired
    OpenSubtitlesClient openSubtitlesClientApi;

//    @Test
    public void testClient() {
        List<SubtitleInfo> big_bang = openSubtitlesClientApi.searchByName("The Lord of the Rings");

        System.out.println(big_bang);

    }
//    @Test
    public void testClientImdbId() {
        List<SubtitleInfo> big_bang = openSubtitlesClientApi.searchByImdb("tt0167260");

        System.out.println(big_bang);

    }

}