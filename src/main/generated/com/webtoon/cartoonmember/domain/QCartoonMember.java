package com.webtoon.cartoonmember.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartoonMember is a Querydsl query type for CartoonMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartoonMember extends EntityPathBase<CartoonMember> {

    private static final long serialVersionUID = 2016294929L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartoonMember cartoonMember = new QCartoonMember("cartoonMember");

    public final com.webtoon.util.QBaseTimeEntity _super = new com.webtoon.util.QBaseTimeEntity(this);

    public final com.webtoon.cartoon.domain.QCartoon cartoon;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDateTime = _super.lastModifiedDateTime;

    public final DateTimePath<java.time.LocalDateTime> lastReadDate = createDateTime("lastReadDate", java.time.LocalDateTime.class);

    public final com.webtoon.member.domain.QMember member;

    public final BooleanPath rated = createBoolean("rated");

    public final BooleanPath thumbsUp = createBoolean("thumbsUp");

    public QCartoonMember(String variable) {
        this(CartoonMember.class, forVariable(variable), INITS);
    }

    public QCartoonMember(Path<? extends CartoonMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartoonMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartoonMember(PathMetadata metadata, PathInits inits) {
        this(CartoonMember.class, metadata, inits);
    }

    public QCartoonMember(Class<? extends CartoonMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cartoon = inits.isInitialized("cartoon") ? new com.webtoon.cartoon.domain.QCartoon(forProperty("cartoon"), inits.get("cartoon")) : null;
        this.member = inits.isInitialized("member") ? new com.webtoon.member.domain.QMember(forProperty("member")) : null;
    }

}

