package ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeDto {

    private Long id;

    private Set<WordDto> words;

    private CollectionDto collectionDto;

    private Integer season;

    private Integer episode;

    private Integer learnedPercent;

}
