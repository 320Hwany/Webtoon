package com.webtoon.content.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentListResult<T> {

    private long count;

    private T cartoonResponseList;

    @Builder
    public ContentListResult(long count, T cartoonResponseList) {
        this.count = count;
        this.cartoonResponseList = cartoonResponseList;
    }
}
