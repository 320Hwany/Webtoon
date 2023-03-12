package com.webtoon.cartoon.dto.request;

import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.*;

import javax.validation.constraints.Min;


@Getter @Setter
@NoArgsConstructor
public class CartoonSearchDto {

    @Min(value = 0, message = "0 이상의 페이지를 입력해주세요")
    private int page;

    private String title;

    private String nickname;

    private long ageRange;

    private String dayOfTheWeek;

    private String progress;
    private String genre;

    @Builder
    public CartoonSearchDto(int page, String title, String nickname, long ageRange,
                            String dayOfTheWeek, String progress, String genre) {
        this.page = page;
        this.title = title;
        this.nickname = nickname;
        this.ageRange = ageRange;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
    }

    public CartoonSearchDto toCartoonEnumField() {
        return CartoonSearchDto.builder()
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
            throw new EnumTypeValidException(isDayValid, isProgressValid, isGenreValid);
        }
    }
}
