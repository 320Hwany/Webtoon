package com.webtoon.comment.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommentUpdate {

    private String commentContent;

    @Builder
    public CommentUpdate(String commentContent) {
        this.commentContent = commentContent;
    }
}
