package com.webtoon.cartoon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.author.domain.QAuthor;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.domain.QCartoon;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.util.enumerated.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.webtoon.author.domain.QAuthor.*;
import static com.webtoon.cartoon.domain.QCartoon.*;

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
    public List<Cartoon> findAllByTitle(CartoonSearch cartoonSearch) {
        return jpaQueryFactory.selectFrom(cartoon)
                .leftJoin(cartoon.author, author)
                .fetchJoin()
                .where(cartoon.title.contains(cartoonSearch.getTitle()))
                .limit(cartoonSearch.getLimit())
                .offset(cartoonSearch.getOffset())
                .fetch();
    }

    @Override
    public List<Cartoon> findAllByGenre(CartoonSearch cartoonSearch) {
        return jpaQueryFactory.selectFrom(cartoon)
                .leftJoin(cartoon.author, author)
                .fetchJoin()
                .where(cartoon.genre.eq(cartoonSearch.getGenre()))
                .limit(cartoonSearch.getLimit())
                .offset(cartoonSearch.getOffset())
                .orderBy(cartoon.likes.desc())
                .fetch();
    }

    @Override
    public List<Cartoon> findAllOrderByLikes(CartoonSearch cartoonSearch) {
        return jpaQueryFactory.selectFrom(cartoon)
                .leftJoin(cartoon.author, author)
                .fetchJoin()
                .limit(cartoonSearch.getLimit())
                .offset(cartoonSearch.getOffset())
                .orderBy(cartoon.likes.desc())
                .fetch();
    }

    @Override
    public void delete(Cartoon cartoon) {
        cartoonJpaRepository.delete(cartoon);
    }

    @Override
    public void saveAll(List<Cartoon> cartoonList) {
        cartoonJpaRepository.saveAll(cartoonList);
    }

    @Override
    public long count() {
        return cartoonJpaRepository.count();
    }
}
