package ru.zagamaza.sublearn.subtitles.client.sublearn.back;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto.NotificationDto;

import java.util.List;

@FeignClient(contextId = "notifications", name = "notifications", url = "${sublearn.back.url}")
@RequestMapping("/notifications")
public interface NotificationClient {

    @GetMapping("/{id}")
    NotificationDto get(@PathVariable("id") Long id);

    @GetMapping("/users/{userId}")
    List<NotificationDto> getByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/users")
    void getByUserId(@RequestParam("notificationText") String notificationText);

    @PostMapping
    NotificationDto create(@RequestBody NotificationDto notificationDto);

    @PutMapping
    NotificationDto update(@RequestBody NotificationDto notificationDto);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);

    @DeleteMapping
    void deleteByIds(@RequestParam("ids") List<Long> ids);

}
