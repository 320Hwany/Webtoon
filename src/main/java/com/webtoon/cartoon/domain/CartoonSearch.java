package com.webtoon.cartoon.domain;

import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.webtoon.util.constant.ConstantCommon.PAGE_LIMIT;

@Getter
@NoArgsConstructor
public class CartoonSearch {

    private final int limit = PAGE_LIMIT;
    private int page;
    private String title;
    private String nickname;

    private long ageRange;

    private DayOfTheWeek dayOfTheWeek;

    private Progress progress;
    private Genre genre;

    @Builder
    public CartoonSearch(Integer page, String title, String nickname, long ageRange,
                         DayOfTheWeek dayOfTheWeek, Progress progress, Genre genre) {
        this.page = page;
        this.title = title;
        this.nickname = nickname;
        this.ageRange = ageRange;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
    }

    public int getOffset() {
        return Math.max(page - 1, 0) * limit;
    }
}
