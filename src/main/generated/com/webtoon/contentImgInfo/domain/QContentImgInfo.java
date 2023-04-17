package com.webtoon.contentImgInfo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentImgInfo is a Querydsl query type for ContentImgInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentImgInfo extends EntityPathBase<ContentImgInfo> {

    private static final long serialVersionUID = 399665177L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentImgInfo contentImgInfo = new QContentImgInfo("contentImgInfo");

    public final com.webtoon.util.QBaseTimeEntity _super = new com.webtoon.util.QBaseTimeEntity(this);

    public final com.webtoon.content.domain.QContent content;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgName = createString("imgName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDateTime = _super.lastModifiedDateTime;

    public QContentImgInfo(String variable) {
        this(ContentImgInfo.class, forVariable(variable), INITS);
    }

    public QContentImgInfo(Path<? extends ContentImgInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentImgInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentImgInfo(PathMetadata metadata, PathInits inits) {
        this(ContentImgInfo.class, metadata, inits);
    }

    public QContentImgInfo(Class<? extends ContentImgInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new com.webtoon.content.domain.QContent(forProperty("content"), inits.get("content")) : null;
    }

}

