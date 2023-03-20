package com.webtoon.cartoon.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static com.webtoon.util.constant.ConstantValid.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonSearchTitle {

    @Min(value = 1, message = PAGE_VALID_MESSAGE)
    private int page;

    @Min(value = 0, message = CARTOON_SIZE_VALID_MESSAGE)
    private int size;

    @NotBlank(message = CARTOON_TITLE_VALID_MESSAGE)
    private String title;

    @Builder
    public CartoonSearchTitle(int page, int size, String title) {
        this.page = page;
        this.size = size;
        this.title = title;
    }
}
