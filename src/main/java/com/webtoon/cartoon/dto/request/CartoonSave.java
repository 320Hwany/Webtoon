package com.webtoon.cartoon.dto.request;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;

import javax.validation.constraints.NotBlank;

import static com.webtoon.cartoon.domain.Cartoon.checkEnumTypeValid;

@Getter
@NoArgsConstructor
public class CartoonSave {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    private String dayOfTheWeek;

    private String progress;

    @Builder
    public CartoonSave(String title, String dayOfTheWeek, String progress) {
        this.title = title;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
    }

    public Cartoon toEntity(Author author) {
        checkEnumTypeValid(dayOfTheWeek, progress);
        return Cartoon.builder()
                .title(title)
                .dayOfTheWeek(DayOfTheWeek.valueOf(dayOfTheWeek))
                .progress(Progress.valueOf(progress))
                .author(author)
                .build();
    }
}
