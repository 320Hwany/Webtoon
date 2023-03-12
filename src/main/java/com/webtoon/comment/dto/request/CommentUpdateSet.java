package com.webtoon.comment.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommentUpdateSet {

    private Long memberSessionId;

    private Long commentId;

    private CommentUpdate commentUpdate;

    @Builder
    public CommentUpdateSet(Long memberSessionId, Long commentId, CommentUpdate commentUpdate) {
        this.memberSessionId = memberSessionId;
        this.commentId = commentId;
        this.commentUpdate = commentUpdate;
    }

    public static CommentUpdateSet getFromParameter(Long memberSessionId, Long commentId, CommentUpdate commentUpdate) {
        return CommentUpdateSet.builder()
                .commentId(commentId)
                .memberSessionId(memberSessionId)
                .commentUpdate(commentUpdate)
                .build();
    }
}
