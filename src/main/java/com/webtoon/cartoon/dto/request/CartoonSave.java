package com.webtoon.cartoon.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CartoonSave {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @Builder
    public CartoonSave(String title) {
        this.title = title;
    }
}
