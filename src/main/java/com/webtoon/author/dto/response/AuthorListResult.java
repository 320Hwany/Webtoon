package com.webtoon.author.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorListResult<T> {

    private long count;

    private T authorResponseList;

    @Builder
    public AuthorListResult(long count, T authorResponseList) {
        this.count = count;
        this.authorResponseList = authorResponseList;
    }
}
