package com.webtoon.cartoon.dto.request;

import lombok.*;

import javax.validation.constraints.Min;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonSearchDto {

    @Min(value = 0, message = "0 이상의 페이지를 입력해주세요")
    private int page;

    private String title;

    private String nickname;

    private long ageRange;

    private String dayOfTheWeek;

    private String progress;
    private String genre;
}
