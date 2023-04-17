package com.webtoon.cartoon.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.webtoon.cartoon.dto.response.QCartoonResponse is a Querydsl Projection type for CartoonResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCartoonResponse extends ConstructorExpression<CartoonResponse> {

    private static final long serialVersionUID = 1460575230L;

    public QCartoonResponse(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<? extends com.webtoon.author.dto.response.AuthorResponse> authorResponse, com.querydsl.core.types.Expression<com.webtoon.util.enumerated.DayOfTheWeek> dayOfTheWeek, com.querydsl.core.types.Expression<com.webtoon.util.enumerated.Progress> progress, com.querydsl.core.types.Expression<com.webtoon.util.enumerated.Genre> genre, com.querydsl.core.types.Expression<Double> rating, com.querydsl.core.types.Expression<Long> likes) {
        super(CartoonResponse.class, new Class<?>[]{long.class, String.class, com.webtoon.author.dto.response.AuthorResponse.class, com.webtoon.util.enumerated.DayOfTheWeek.class, com.webtoon.util.enumerated.Progress.class, com.webtoon.util.enumerated.Genre.class, double.class, long.class}, id, title, authorResponse, dayOfTheWeek, progress, genre, rating, likes);
    }

}

