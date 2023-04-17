package com.webtoon.contentmember.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentMember is a Querydsl query type for ContentMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentMember extends EntityPathBase<ContentMember> {

    private static final long serialVersionUID = -787663631L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentMember contentMember = new QContentMember("contentMember");

    public final com.webtoon.content.domain.QContent content;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.webtoon.member.domain.QMember member;

    public QContentMember(String variable) {
        this(ContentMember.class, forVariable(variable), INITS);
    }

    public QContentMember(Path<? extends ContentMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentMember(PathMetadata metadata, PathInits inits) {
        this(ContentMember.class, metadata, inits);
    }

    public QContentMember(Class<? extends ContentMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new com.webtoon.content.domain.QContent(forProperty("content"), inits.get("content")) : null;
        this.member = inits.isInitialized("member") ? new com.webtoon.member.domain.QMember(forProperty("member")) : null;
    }

}

