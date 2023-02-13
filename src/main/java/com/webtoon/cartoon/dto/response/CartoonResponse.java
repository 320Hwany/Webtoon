package com.webtoon.cartoon.dto.response;

import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CartoonResponse {

    private String title;
    private AuthorResponse authorResponse;

    private DayOfTheWeek dayOfTheWeek;

    private Progress progress;
    private Genre genre;

    private double rating;
    private long likes;

    @Builder
    public CartoonResponse(String title, AuthorResponse authorResponse, DayOfTheWeek dayOfTheWeek, Progress progress,
                           Genre genre, double rating, long likes) {
        this.title = title;
        this.authorResponse = authorResponse;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
        this.rating = rating;
        this.likes = likes;
    }

    public static CartoonResponse getFromCartoon(Cartoon cartoon) {
        return CartoonResponse.builder()
                .title(cartoon.getTitle())
                .authorResponse(AuthorResponse.getFromAuthor(cartoon.getAuthor()))
                .dayOfTheWeek(cartoon.getDayOfTheWeek())
                .progress(cartoon.getProgress())
                .genre(cartoon.getGenre())
                .rating(cartoon.getRating())
                .likes(cartoon.getLikes())
                .build();
    }

    public static List<CartoonResponse> getFromCartoonList(List<Cartoon> cartoonList) {
        return cartoonList.stream()
                .map(CartoonResponse::getFromCartoon)
                .collect(Collectors.toList());
    }
}
