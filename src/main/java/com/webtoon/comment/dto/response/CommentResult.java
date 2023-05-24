package com.webtoon.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommentResult<T> {

    private long count;

    private T commentResponse;

    @Builder
    public CommentResult(long count, T commentResponse) {
        this.count = count;
        this.commentResponse = commentResponse;
    }
}
