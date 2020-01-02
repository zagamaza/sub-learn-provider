package ru.zagamaza.sublearn.subtitles.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.zagamaza.sublearn.subtitles.controller.SubtitleController;

@SpringBootTest
class IntegrationTest {

    @Autowired
    SubtitleController subtitleController;

    @Test
    public void testClient() {
        subtitleController.upload("tt9561862");
    }




}