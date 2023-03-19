package com.webtoon.cartoon.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.domain.QCartoon;
import com.webtoon.cartoon.dto.request.CartoonSearchTitle;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.webtoon.author.domain.QAuthor.author;
import static com.webtoon.cartoon.domain.QCartoon.cartoon;
import static org.springframework.data.domain.Sort.Direction.*;


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
    public List<Cartoon> findAllByTitle(CartoonSearchTitle cartoonSearchTitle) {
        PageRequest pageRequest = PageRequest.of(cartoonSearchTitle.getPage(), cartoonSearchTitle.getSize(),
                Sort.by(DESC, "likes"));
        return cartoonJpaRepository.findAllByTitleContains(cartoonSearchTitle.getTitle(), pageRequest);
    }

    @Override
    public List<Cartoon> findAllByCartoonCondOrderByLikes(CartoonSearch cartoonSearch) {
        return jpaQueryFactory
                .selectFrom(cartoon)
                .leftJoin(cartoon.author, author)
                .fetchJoin()
                .where(
                        dayOfTheWeekEq(cartoonSearch.getDayOfTheWeek()),
                        genreEq(cartoonSearch.getGenre()),
                        progressEq(cartoonSearch.getProgress())
                )
                .orderBy(cartoon.likes.desc())
                .offset(cartoonSearch.getOffset())
                .limit(cartoonSearch.getLimit())
                .fetch();
    }

    @Override
    public List<Cartoon> findAllByCartoonCondOrderByRating(CartoonSearch cartoonSearch) {
        return jpaQueryFactory
                .selectFrom(cartoon)
                .leftJoin(cartoon.author, author)
                .fetchJoin()
                .where(
                        dayOfTheWeekEq(cartoonSearch.getDayOfTheWeek()),
                        genreEq(cartoonSearch.getGenre()),
                        progressEq(cartoonSearch.getProgress())
                )
                .orderBy(cartoon.rating.desc())
                .offset(cartoonSearch.getOffset())
                .limit(cartoonSearch.getLimit())
                .fetch();
    }

    private BooleanExpression dayOfTheWeekEq(DayOfTheWeek dayOfTheWeek) {
        return dayOfTheWeek != DayOfTheWeek.NONE ? cartoon.dayOfTheWeek.eq(dayOfTheWeek) : null;
    }

    private BooleanExpression genreEq(Genre genre) {
        return genre != Genre.NONE ? cartoon.genre.eq(genre) : null;
    }

    private BooleanExpression progressEq(Progress progress) {
        return progress != Progress.NONE ? cartoon.progress.eq(progress) : null;
    }

    @Override
    public void delete(Cartoon cartoon) {
        cartoonJpaRepository.delete(cartoon);
    }

    @Override
    public void deleteAll() {
        cartoonJpaRepository.deleteAll();
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
