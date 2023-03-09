package com.webtoon.content.dto.request;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_DOUBLE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentSave {

    @NotBlank(message = "부제를 입력해주세요")
    private String subTitle;

    @Min(value = 0, message = "몇 화인지를 입력해주세요")
    private int episode;

    @NotNull(message = "등록 날짜를 입력해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
}
