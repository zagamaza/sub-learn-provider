package ru.zagamaza.sublearn.subtitles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.CollectionClientApi;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.CollectionCondensedDto;
import ru.zagamaza.sublearn.subtitles.dto.FoundCollection;
import ru.zagamaza.sublearn.subtitles.dto.Season;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SubtitlesScheduledUploadService {

    public static final long MAZANUR_USER_ID = 1L;

    @Value("${upload.not.finish.serials}")
    private boolean isUploadNotFinishedSerials;

    private final CollectionClientApi collectionClientApi;
    private final SubtitlesService subtitlesService;
    private final SubtitlesUploadService subtitlesUploadService;
    private final ImdbSearchService imdbSearchService;

    /**
     * Метод uploadNotFinishSerials, каждые день в 23:00 проверяет не законченнные сериалы,
     * на наличие новых не загруженных серий
     */
    @Scheduled(cron = "0 0 23 * * ?", zone = "Europe/Moscow")
    private void uploadNotFinishSerials() {
        if (!isUploadNotFinishedSerials){
            return;
        }
        List<CollectionCondensedDto> serials = collectionClientApi.findNotFinishedSerials();
        serials.forEach(s -> {
            FoundCollection foundCollection = imdbSearchService.findCollectionByImdbId(s.getImdbId());
            List<Season> subtitles = subtitlesService.getSubtitles(s.getImdbId());
            subtitlesUploadService.upload(foundCollection, subtitles, MAZANUR_USER_ID);
        });
    }

}
