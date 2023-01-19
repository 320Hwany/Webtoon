package com.webtoon.content.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ContentSave {

    @NotBlank(message = "부제를 입력해주세요")
    private String subTitle;

    @Min(value = 0, message = "몇 화인지를 입력해주세요")
    private Integer episode;

    @NotNull(message = "등록 날짜를 입력해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    @Builder
    public ContentSave(String subTitle, Integer episode, LocalDate registrationDate) {
        this.subTitle = subTitle;
        this.episode = episode;
        this.registrationDate = registrationDate;
    }
}
