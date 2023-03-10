package com.webtoon.comment.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.webtoon.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommentResponse {

    private Long commentId;

    private Long contentId;

    private String commentContent;

    private String nickname;

    private long likes;

    @Builder
    @QueryProjection
    public CommentResponse(Long commentId, Long contentId, String commentContent,
                           String nickname, long likes) {
        this.commentId = commentId;
        this.contentId = contentId;
        this.commentContent = commentContent;
        this.nickname = nickname;
        this.likes = likes;
    }

    public static CommentResponse getFromEntity(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .contentId(comment.getContent().getId())
                .commentContent(comment.getCommentContent())
                .nickname(comment.getMember().getNickname())
                .likes(comment.getLikes())
                .build();
    }
}
