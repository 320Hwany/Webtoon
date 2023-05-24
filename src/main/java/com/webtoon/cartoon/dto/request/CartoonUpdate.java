package com.webtoon.cartoon.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.webtoon.util.constant.ConstantValid.CARTOON_TITLE_VALID_MESSAGE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonUpdate {

    @NotBlank(message = CARTOON_TITLE_VALID_MESSAGE)
    private String title;

    private String dayOfTheWeek;

    private String progress;

    private String genre;

    @Builder
    private CartoonUpdate(String title, String dayOfTheWeek, String progress, String genre) {
        this.title = title;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
    }
}
