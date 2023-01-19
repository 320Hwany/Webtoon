package com.webtoon.content.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.content.domain.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ContentRepositoryImpl implements ContentRepository {

    private final ContentJpaRepository contentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Content save(Content content) {
        return contentJpaRepository.save(content);
    }
}
