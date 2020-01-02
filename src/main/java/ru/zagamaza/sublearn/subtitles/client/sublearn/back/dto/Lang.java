package ru.zagamaza.sublearn.subtitles.client.sublearn.back.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Lang {
    RU_EN("ru-us"),
    EN_RU("en-ru");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
