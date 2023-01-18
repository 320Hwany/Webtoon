package com.webtoon.cartoon.dto.request;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CartoonUpdate {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    private String dayOfTheWeek;

    private String progress;

    private String genre;

    @Builder
    public CartoonUpdate(String title, String dayOfTheWeek, String progress, String genre) {
        this.title = title;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
    }
}
