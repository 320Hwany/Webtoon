package com.webtoon.cartoon.dto.request;

import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.exception.CartoonEnumTypeException;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.*;

import javax.validation.constraints.Min;

import static com.webtoon.util.constant.ConstantValid.PAGE_VALID_MESSAGE;


@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonSearchCond {

    @Min(value = 1, message = PAGE_VALID_MESSAGE)
    private int page;

    private String title;

    private String nickname;

    private long ageRange;

    private String dayOfTheWeek;

    private String progress;
    private String genre;

    @Builder
    private CartoonSearchCond(int page, String title, String nickname, long ageRange,
                             String dayOfTheWeek, String progress, String genre) {
        this.page = page;
        this.title = title;
        this.nickname = nickname;
        this.ageRange = ageRange;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
    }

    public CartoonSearch toCartoonSearch() {
        return CartoonSearch.builder()
                .page(page)
                .title(title)
                .nickname(nickname)
                .ageRange(ageRange)
                .dayOfTheWeek(DayOfTheWeek.valueOf(dayOfTheWeek))
                .progress(Progress.valueOf(progress))
                .genre(Genre.valueOf(genre))
                .build();
    }

    public CartoonSearchCond toCartoonEnumField() {
        return CartoonSearchCond.builder()
                .page(page)
                .title(title)
                .nickname(nickname)
                .ageRange(ageRange)
                .dayOfTheWeek(dayOfTheWeek != null ? dayOfTheWeek : "NONE")
                .genre(genre != null ? genre : "NONE")
                .progress(progress != null ? progress : "NONE")
                .build();
    }

    public void validateEnumTypeValid() {
        boolean isDayValid = DayOfTheWeek.validateValid(dayOfTheWeek);
        boolean isProgressValid = Progress.validateValid(progress);
        boolean isGenreValid = Genre.validateValid(genre);

        if ((!isDayValid) || (!isProgressValid) || (!isGenreValid)) {
            throw new CartoonEnumTypeException(isDayValid, isProgressValid, isGenreValid);
        }
    }
}
