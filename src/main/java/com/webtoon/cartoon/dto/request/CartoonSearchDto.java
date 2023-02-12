package com.webtoon.cartoon.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
public class CartoonSearchDto {

    @Min(value = 1, message = "1 이상의 페이지를 입력해주세요")
    private int page;

    private String title;

    private String nickName;

    private String dayOfTheWeek;

    private String progress;
    private String genre;

    @Builder
    public CartoonSearchDto(int page, String title, String nickName,
                            String dayOfTheWeek, String progress, String genre) {
        this.page = page;
        this.title = title;
        this.nickName = nickName;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
    }
}
