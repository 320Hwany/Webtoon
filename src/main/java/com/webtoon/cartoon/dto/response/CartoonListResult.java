package com.webtoon.cartoon.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonListResult<T> {

    private long count;

    private T cartoonResponseList;

    @Builder
    public CartoonListResult(long count, T cartoonResponseList) {
        this.count = count;
        this.cartoonResponseList = cartoonResponseList;
    }
}
