package com.webtoon.cartoon.dto.response;

import com.webtoon.author.domain.Author;
import com.webtoon.util.embedded.DayOfTheWeek;
import com.webtoon.util.embedded.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartoonResponse {

    private String title;

    private DayOfTheWeek dayOfTheWeek;

    private Progress progress;
    private Author author;

    @Builder
    public CartoonResponse(String title, DayOfTheWeek dayOfTheWeek, Progress progress, Author author) {
        this.title = title;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.author = author;
    }
}
