package com.webtoon.content.dto.request;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.webtoon.util.constant.ConstantCommon.ZERO_OF_TYPE_DOUBLE;
import static com.webtoon.util.constant.ConstantValid.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentSave {

    @NotBlank(message = SUBTITLE_VALID_MESSAGE)
    private String subTitle;

    @Min(value = 0, message = EPISODE_VALID_MESSAGE)
    private int episode;

    @NotNull(message = REGISTRATION_DATE_VALID_MESSAGE)
    @DateTimeFormat(pattern = YEAR_MONTH_DAY)
    private LocalDate registrationDate;

    public Content toEntity(Cartoon cartoon) {
        return Content.builder()
                .cartoon(cartoon)
                .subTitle(subTitle)
                .episode(episode)
                .registrationDate(registrationDate)
                .rating(ZERO_OF_TYPE_DOUBLE)
                .build();
    }

    @Builder
    public ContentSave(String subTitle, int episode, LocalDate registrationDate) {
        this.subTitle = subTitle;
        this.episode = episode;
        this.registrationDate = registrationDate;
    }
}
