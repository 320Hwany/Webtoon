package com.webtoon.content.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentUpdate {

    @NotBlank(message = "부제를 입력해주세요")
    private String subTitle;

    @Min(value = 0, message = "몇 화인지를 입력해주세요")
    private int episode;

    @NotNull(message = "변경할 등록 날짜를 입력해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
}
