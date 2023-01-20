package com.webtoon.content.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.content.domain.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.webtoon.cartoon.domain.QCartoon.*;
import static com.webtoon.content.domain.QContent.*;

@RequiredArgsConstructor
@Repository
public class ContentRepositoryImpl implements ContentRepository {

    private final ContentJpaRepository contentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Content save(Content content) {
        return contentJpaRepository.save(content);
    }

    @Override
    public Optional<Content> findByCartoonAndEpisode(Long cartoonId, Integer episode) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(content)
                .leftJoin(content.cartoon, cartoon)
                .fetchJoin()
                .where(content.cartoon.id.eq(cartoon.id))
                .where(content.episode.eq(episode))
                .fetchOne());
    }
}
