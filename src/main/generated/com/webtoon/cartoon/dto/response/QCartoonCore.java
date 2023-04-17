package com.webtoon.cartoon.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.webtoon.cartoon.dto.response.QCartoonCore is a Querydsl Projection type for CartoonCore
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCartoonCore extends ConstructorExpression<CartoonCore> {

    private static final long serialVersionUID = -1353605668L;

    public QCartoonCore(com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<Long> likes) {
        super(CartoonCore.class, new Class<?>[]{String.class, String.class, long.class}, title, nickname, likes);
    }

}

