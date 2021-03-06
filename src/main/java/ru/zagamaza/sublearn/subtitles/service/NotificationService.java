package ru.zagamaza.sublearn.subtitles.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.NotificationClient;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.NotificationDto;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.NotificationType;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.UserDto;
import ru.zagamaza.sublearn.subtitles.dto.FoundCollection;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final String COLLECTION_UPLOAD = "🎉 Коллекция *%s* успешно загружена\nМожете найти ее в своих коллекциях.";
    private static final String COLLECTION_FAIL = "😓 К сожалению, мы не смогли загрузить коллекцию *%s*.";


    private final NotificationClient notificationClient;

    public void sendFailNotification(FoundCollection foundCollection, Long userId) {
        log.error(String.format("Collection %s upload failed", foundCollection.getTitle()));
        NotificationDto notification = NotificationDto.builder()
                                                      .notificationType(NotificationType.MESSAGE)
                                                      .userDto(UserDto.builder().id(userId).build())
                                                      .text(String.format(COLLECTION_FAIL, foundCollection.getTitle()))
                                                      .build();
        notificationClient.create(notification);
    }

    public void sendSuccessNotification(FoundCollection foundCollection, Long userId) {
        log.info(String.format("Collection %s upload success", foundCollection.getTitle()));
        NotificationDto notification = NotificationDto.builder()
                                                      .notificationType(NotificationType.MESSAGE)
                                                      .userDto(UserDto.builder().id(userId).build())
                                                      .text(String.format(COLLECTION_UPLOAD, foundCollection.getTitle()))
                                                      .build();
        notificationClient.create(notification);
    }

}
