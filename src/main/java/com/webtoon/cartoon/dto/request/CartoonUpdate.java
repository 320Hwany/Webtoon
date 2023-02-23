package com.webtoon.cartoon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonUpdate {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    private String dayOfTheWeek;

    private String progress;

    private String genre;
}
