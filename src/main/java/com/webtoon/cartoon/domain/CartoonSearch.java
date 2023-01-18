package com.webtoon.cartoon.domain;

import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartoonSearch {

    private final Integer limit = 20;
    private Integer page;
    private String title;
    private String nickName;

    private DayOfTheWeek dayOfTheWeek;

    private Progress progress;
    private Genre genre;

    @Builder
    public CartoonSearch(Integer page, String title, String nickName,
                         DayOfTheWeek dayOfTheWeek, Progress progress, Genre genre) {
        this.page = page;
        this.title = title;
        this.nickName = nickName;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
    }

    public Integer getOffset() {
        return Math.max(page - 1, 0) * limit;
    }

    public static CartoonSearch getByCartoonSearchDto(CartoonSearchDto cartoonSearchDto) {
        return CartoonSearch.builder()
                .page(cartoonSearchDto.getPage())
                .title(cartoonSearchDto.getTitle())
                .nickName(cartoonSearchDto.getNickName())
                .dayOfTheWeek(DayOfTheWeek.valueOf(cartoonSearchDto.getDayOfTheWeek()))
                .progress(Progress.valueOf(cartoonSearchDto.getProgress()))
                .genre(Genre.valueOf(cartoonSearchDto.getGenre()))
                .build();
    }
}
