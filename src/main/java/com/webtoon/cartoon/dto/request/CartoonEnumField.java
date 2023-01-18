package com.webtoon.cartoon.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartoonEnumField {

    private String dayOfTheWeek;

    private String progress;

    private String genre;

    @Builder
    public CartoonEnumField(String dayOfTheWeek, String progress, String genre) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
    }

    public static CartoonEnumField getFromCartoonSave(CartoonSave cartoonSave) {
        return CartoonEnumField.builder()
                .dayOfTheWeek(cartoonSave.getDayOfTheWeek())
                .progress(cartoonSave.getProgress())
                .genre(cartoonSave.getGenre())
                .build();
    }

    public static CartoonEnumField getFromCartoonUpdate(CartoonUpdate cartoonUpdate) {
        return CartoonEnumField.builder()
                .dayOfTheWeek(cartoonUpdate.getDayOfTheWeek())
                .progress(cartoonUpdate.getProgress())
                .genre(cartoonUpdate.getGenre())
                .build();
    }
}
