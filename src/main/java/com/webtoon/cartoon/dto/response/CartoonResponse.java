package com.webtoon.cartoon.dto.response;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Progress;
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

    public static CartoonResponse getFromCartoon(Cartoon cartoon) {
        return CartoonResponse.builder()
                .title(cartoon.getTitle())
                .dayOfTheWeek(cartoon.getDayOfTheWeek())
                .progress(cartoon.getProgress())
                .author(cartoon.getAuthor())
                .build();
    }
}
