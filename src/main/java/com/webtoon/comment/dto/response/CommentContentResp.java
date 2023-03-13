package com.webtoon.comment.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommentContentResp {

    private Long commentId;

    private String commentContent;

    private String nickname;

    private long likes;

    private LocalDateTime createDateTime;

    @Builder
    @QueryProjection
    public CommentContentResp(Long commentId, String commentContent, String nickname,
                              long likes, LocalDateTime createDateTime) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.nickname = nickname;
        this.likes = likes;
        this.createDateTime = createDateTime;
    }
}
