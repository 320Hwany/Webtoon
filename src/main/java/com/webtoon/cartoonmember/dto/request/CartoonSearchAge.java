package com.webtoon.cartoonmember.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

import static com.webtoon.util.constant.ConstantValid.*;
import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class CartoonSearchAge {

    @Min(value = 0, message = PAGE_VALID_MESSAGE)
    private int page;

    @Min(value = 0, message = CARTOON_SIZE_VALID_MESSAGE)
    private int size;

    @Min(value = 1, message = AGE_VALID_MESSAGE)
    private long ageRange;

    @Builder
    public CartoonSearchAge(int page, int size, long ageRange) {
        this.page = page;
        this.size = size;
        this.ageRange = ageRange;
    }

    public int getOffset() {
        return Math.max(page - 1, 0) * size;
    }
}
