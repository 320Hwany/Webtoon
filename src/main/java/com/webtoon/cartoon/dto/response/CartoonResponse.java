package com.webtoon.cartoon.dto.response;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CartoonResponse {

    private String title;
    private Author author;

    private DayOfTheWeek dayOfTheWeek;

    private Progress progress;
    private Genre genre;

    private Long rating;
    private Integer likes;

    @Builder
    public CartoonResponse(String title, Author author, DayOfTheWeek dayOfTheWeek, Progress progress,
                           Genre genre, Long rating, Integer likes) {
        this.title = title;
        this.author = author;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
        this.rating = rating;
        this.likes = likes;
    }

    public static CartoonResponse getFromCartoon(Cartoon cartoon) {
        return CartoonResponse.builder()
                .title(cartoon.getTitle())
                .author(cartoon.getAuthor())
                .dayOfTheWeek(cartoon.getDayOfTheWeek())
                .progress(cartoon.getProgress())
                .genre(cartoon.getGenre())
                .build();
    }

    public static List<CartoonResponse> getFromCartoonList(List<Cartoon> cartoonList) {
        return cartoonList.stream()
                .map(CartoonResponse::getFromCartoon)
                .collect(Collectors.toList());
    }
}
