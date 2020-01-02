package ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionRequest {

    private Long id;

    private String imdbId;

    @NotNull
    private Lang lang;

    @NotNull
    private Long userId;

    @NotNull
    private String name;

    private String url;

    private Integer rating;

    private boolean isShared;

    private LocalDateTime created;

    private boolean isSerial;

}
