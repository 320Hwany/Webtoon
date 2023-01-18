package com.webtoon.cartoon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.author.domain.QAuthor;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.QCartoon;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.util.enumerated.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CartoonRepositoryImpl implements CartoonRepository {

    private final CartoonJpaRepository cartoonJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Cartoon save(Cartoon cartoon) {
        return cartoonJpaRepository.save(cartoon);
    }

    @Override
    public Cartoon getById(Long id) {
        return cartoonJpaRepository.findById(id)
                .orElseThrow(CartoonNotFoundException::new);
    }

    @Override
    public Cartoon getByTitle(String title) {
        return cartoonJpaRepository.findByTitle(title)
                .orElseThrow(CartoonNotFoundException::new);
    }

    @Override
    public List<Cartoon> findAllByGenre(Genre genre) {
        return jpaQueryFactory.selectFrom(QCartoon.cartoon)
                .leftJoin(QCartoon.cartoon.author, QAuthor.author)
                .fetchJoin()
                .where(QCartoon.cartoon.genre.eq(genre))
                .orderBy(QCartoon.cartoon.likes.desc())
                .fetch();
    }
}
