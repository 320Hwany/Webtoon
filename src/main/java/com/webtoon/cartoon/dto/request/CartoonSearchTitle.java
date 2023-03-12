package com.webtoon.cartoon.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CartoonSearchTitle {

    @Min(value = 0, message = "0 이상의 페이지를 입력해주세요")
    private int page;

    @Min(value = 0, message = "0 이상의 사이즈를 입력해주세요")
    private int size;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @Builder
    public CartoonSearchTitle(int page, int size, String title) {
        this.page = page;
        this.size = size;
        this.title = title;
    }
}
