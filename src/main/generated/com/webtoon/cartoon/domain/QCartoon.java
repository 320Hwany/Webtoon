package com.webtoon.cartoon.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartoon is a Querydsl query type for Cartoon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartoon extends EntityPathBase<Cartoon> {

    private static final long serialVersionUID = 1248936881L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartoon cartoon = new QCartoon("cartoon");

    public final com.webtoon.util.QBaseTimeEntity _super = new com.webtoon.util.QBaseTimeEntity(this);

    public final com.webtoon.author.domain.QAuthor author;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final EnumPath<com.webtoon.util.enumerated.DayOfTheWeek> dayOfTheWeek = createEnum("dayOfTheWeek", com.webtoon.util.enumerated.DayOfTheWeek.class);

    public final EnumPath<com.webtoon.util.enumerated.Genre> genre = createEnum("genre", com.webtoon.util.enumerated.Genre.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDateTime = _super.lastModifiedDateTime;

    public final NumberPath<Long> likes = createNumber("likes", Long.class);

    public final EnumPath<com.webtoon.util.enumerated.Progress> progress = createEnum("progress", com.webtoon.util.enumerated.Progress.class);

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final StringPath title = createString("title");

    public QCartoon(String variable) {
        this(Cartoon.class, forVariable(variable), INITS);
    }

    public QCartoon(Path<? extends Cartoon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartoon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartoon(PathMetadata metadata, PathInits inits) {
        this(Cartoon.class, metadata, inits);
    }

    public QCartoon(Class<? extends Cartoon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.webtoon.author.domain.QAuthor(forProperty("author")) : null;
    }

}

