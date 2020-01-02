package ru.zagamaza.sublearn.subtitles.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Collection {
    private String title;
    private Boolean isSeries;
    private List<Season> seasons;
}
