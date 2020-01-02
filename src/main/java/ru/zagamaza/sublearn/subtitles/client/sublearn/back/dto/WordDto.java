package ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordDto {

    private Long id;
    private String word;
    private String transcription;
    private String mainTranslation;
    private List<TranslationDto> translation;
    private String lang;
    private LocalDateTime created;

    public WordDto(Long id) {
        this.id = id;
    }

}
