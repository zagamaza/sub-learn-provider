package ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto {

    private Long id;

    private String imdbId;

    private List<EpisodeDto> episodeDtos;

    private Long userId;

    @NotNull
    private Lang lang;

    @NotNull
    private String name;

    private String url;

    private Integer rating;

    private boolean isShared;

    private boolean isSerial;

    private LocalDateTime created;


}
