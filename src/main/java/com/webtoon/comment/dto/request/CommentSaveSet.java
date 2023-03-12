package com.webtoon.comment.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommentSaveSet {

    private CommentSave commentSave;

    private Long memberSessionId;

    private Long contentId;

    @Builder
    public CommentSaveSet(CommentSave commentSave, Long memberSessionId, Long contentId) {
        this.commentSave = commentSave;
        this.memberSessionId = memberSessionId;
        this.contentId = contentId;
    }

    public static CommentSaveSet getFromParameter(CommentSave commentSave, Long memberSessionId, Long contentId) {
        return CommentSaveSet.builder()
                .commentSave(commentSave)
                .memberSessionId(memberSessionId)
                .contentId(contentId)
                .build();
    }
}
