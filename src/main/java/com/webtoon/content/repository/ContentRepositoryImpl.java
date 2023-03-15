package com.webtoon.content.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.cartoon.domain.QCartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.content.domain.QContent;
import com.webtoon.content.exception.ContentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.Order.DESC;
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
    public List<Content> findAllByCartoonId(Long cartoonId, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(content)
                .leftJoin(content.cartoon, cartoon)
                .fetchJoin()
                .where(cartoon.id.eq(cartoonId))
                .orderBy(content.episode.desc())
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Optional<Content> findByCartoonIdAndEpisode(Long cartoonId, int episode) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(content)
                        .leftJoin(content.cartoon, cartoon)
                        .fetchJoin()
                        .leftJoin(cartoon.author, author)
                        .fetchJoin()
                        .where(cartoon.id.eq(cartoonId),
                                content.episode.eq(episode))
                        .fetchOne());
    }

    @Override
    public void saveAll(List<Content> contentList) {
        contentJpaRepository.saveAll(contentList);
    }

    @Override
    public void deleteAll() {
        contentJpaRepository.deleteAll();
    }

    @Override
    public long count() {
        return contentJpaRepository.count();
    }
}
