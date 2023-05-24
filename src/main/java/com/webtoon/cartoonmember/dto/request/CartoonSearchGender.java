package com.webtoon.cartoonmember.dto.request;

import lombok.*;

import javax.validation.constraints.Min;

import static com.webtoon.util.constant.ConstantValid.CARTOON_SIZE_VALID_MESSAGE;
import static com.webtoon.util.constant.ConstantValid.PAGE_VALID_MESSAGE;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonSearchGender {

    @Min(value = 0, message = PAGE_VALID_MESSAGE)
    private int page;

    @Min(value = 0, message = CARTOON_SIZE_VALID_MESSAGE)
    private int size;

    private String gender;

    @Builder
    private CartoonSearchGender(int page, int size, String gender) {
        this.page = page;
        this.size = size;
        this.gender = gender;
    }

    public int getOffset() {
        return Math.max(page - 1, 0) * size;
    }
}
