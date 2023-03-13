package com.webtoon.cartoon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.webtoon.util.constant.ConstantValid.CARTOON_TITLE_VALID_MESSAGE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonUpdate {

    @NotBlank(message = CARTOON_TITLE_VALID_MESSAGE)
    private String title;

    private String dayOfTheWeek;

    private String progress;

    private String genre;
}
