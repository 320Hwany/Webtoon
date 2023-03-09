package com.webtoon.cartoon.dto.request;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_DOUBLE;
import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_LONG;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonSave {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    private String dayOfTheWeek;

    private String progress;

    private String genre;

    public Cartoon toEntity(Author author) {
        Cartoon cartoon = Cartoon.builder()
                .title(title)
                .author(author)
                .dayOfTheWeek(DayOfTheWeek.valueOf(dayOfTheWeek))
                .progress(Progress.valueOf(progress))
                .genre(Genre.valueOf(genre))
                .rating(ZERO_OF_TYPE_DOUBLE)
                .likes(ZERO_OF_TYPE_LONG)
                .build();

        author.getCartoonList().add(cartoon);
        return cartoon;
    }
}
