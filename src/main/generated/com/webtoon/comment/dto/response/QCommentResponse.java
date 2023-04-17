package com.webtoon.comment.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.webtoon.comment.dto.response.QCommentResponse is a Querydsl Projection type for CommentResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentResponse extends ConstructorExpression<CommentResponse> {

    private static final long serialVersionUID = 566062238L;

    public QCommentResponse(com.querydsl.core.types.Expression<Long> commentId, com.querydsl.core.types.Expression<Long> contentId, com.querydsl.core.types.Expression<String> commentContent, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<Long> likes) {
        super(CommentResponse.class, new Class<?>[]{long.class, long.class, String.class, String.class, long.class}, commentId, contentId, commentContent, nickname, likes);
    }

}

