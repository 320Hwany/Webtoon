package com.webtoon.cartoonmember.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.webtoon.cartoonmember.dto.response.QCartoonMemberResponse is a Querydsl Projection type for CartoonMemberResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCartoonMemberResponse extends ConstructorExpression<CartoonMemberResponse> {

    private static final long serialVersionUID = 1659071198L;

    public QCartoonMemberResponse(com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> authorNickname, com.querydsl.core.types.Expression<java.time.LocalDateTime> lastReadDate) {
        super(CartoonMemberResponse.class, new Class<?>[]{String.class, String.class, java.time.LocalDateTime.class}, title, authorNickname, lastReadDate);
    }

}

