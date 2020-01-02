package ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RestPageImpl<T> {
    private List<T> content;
    private Integer numebr;
    private Integer size;
}
