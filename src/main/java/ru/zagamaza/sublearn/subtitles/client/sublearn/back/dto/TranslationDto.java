package ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TranslationDto {

    private Long id;
    private String partSpeech;
    private List<String> translate;


}
