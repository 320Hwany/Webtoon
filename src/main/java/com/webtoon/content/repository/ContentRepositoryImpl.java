package com.webtoon.content.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.content.domain.Content;
import com.webtoon.content.exception.ContentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.webtoon.author.domain.QAuthor.author;
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
    public Content getById(Long id) {
        return contentJpaRepository.findById(id)
                .orElseThrow(ContentNotFoundException::new);
    }

    @Override
    public Optional<Content> findByCartoonIdAndEpisode(Long cartoonId, int episode) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(content)
                .leftJoin(content.cartoon, cartoon)
                .fetchJoin()
                .where(content.cartoon.id.eq(cartoonId))
                .where(content.episode.eq(episode))
                .leftJoin(cartoon.author, author)
                .fetchJoin()
                .where(cartoon.author.id.eq(author.id))
                .fetchOne());
    }
}
