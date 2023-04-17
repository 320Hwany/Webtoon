package com.webtoon.author.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthor is a Querydsl query type for Author
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthor extends EntityPathBase<Author> {

    private static final long serialVersionUID = -31710401L;

    public static final QAuthor author = new QAuthor("author");

    public final com.webtoon.util.QBaseTimeEntity _super = new com.webtoon.util.QBaseTimeEntity(this);

    public final ListPath<com.webtoon.cartoon.domain.Cartoon, com.webtoon.cartoon.domain.QCartoon> cartoonList = this.<com.webtoon.cartoon.domain.Cartoon, com.webtoon.cartoon.domain.QCartoon>createList("cartoonList", com.webtoon.cartoon.domain.Cartoon.class, com.webtoon.cartoon.domain.QCartoon.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDateTime = _super.lastModifiedDateTime;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public QAuthor(String variable) {
        super(Author.class, forVariable(variable));
    }

    public QAuthor(Path<? extends Author> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthor(PathMetadata metadata) {
        super(Author.class, metadata);
    }

}

