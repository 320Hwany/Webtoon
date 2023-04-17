package com.webtoon.comment.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.webtoon.comment.dto.response.QCommentContentResp is a Querydsl Projection type for CommentContentResp
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentContentResp extends ConstructorExpression<CommentContentResp> {

    private static final long serialVersionUID = -718139796L;

    public QCommentContentResp(com.querydsl.core.types.Expression<Long> commentId, com.querydsl.core.types.Expression<String> commentContent, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<Long> likes, com.querydsl.core.types.Expression<java.time.LocalDateTime> createDateTime) {
        super(CommentContentResp.class, new Class<?>[]{long.class, String.class, String.class, long.class, java.time.LocalDateTime.class}, commentId, commentContent, nickname, likes, createDateTime);
    }

}

