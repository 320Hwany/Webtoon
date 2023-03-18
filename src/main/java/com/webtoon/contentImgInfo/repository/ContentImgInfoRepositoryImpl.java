package com.webtoon.contentImgInfo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.webtoon.content.domain.QContent.*;
import static com.webtoon.contentImgInfo.domain.QContentImgInfo.*;


@RequiredArgsConstructor
@Repository
public class ContentImgInfoRepositoryImpl implements ContentImgInfoRepository {

    private final ContentImgInfoJpaRepository contentImgInfoJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(ContentImgInfo contentImgInfo) {
        contentImgInfoJpaRepository.save(contentImgInfo);
    }

    // todo oneToOne
    @Override
    public Optional<ContentImgInfo> findByContentId(Long contentId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(contentImgInfo)
                .leftJoin(contentImgInfo.content, content)
                .fetchJoin()
                .where(content.id.eq(contentId))
                .fetchFirst());
    }

    @Override
    public long count() {
        return contentImgInfoJpaRepository.count();
    }
}
