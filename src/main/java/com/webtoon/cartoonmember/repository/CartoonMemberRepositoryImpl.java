package com.webtoon.cartoonmember.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.author.domain.QAuthor;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.domain.QCartoon;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.domain.QCartoonMember;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.member.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.webtoon.author.domain.QAuthor.author;
import static com.webtoon.cartoon.domain.QCartoon.*;
import static com.webtoon.cartoonmember.domain.QCartoonMember.cartoonMember;
import static com.webtoon.member.domain.QMember.member;
import static java.lang.Boolean.TRUE;


@RequiredArgsConstructor
@Repository
public class CartoonMemberRepositoryImpl implements CartoonMemberRepository {

    private final CartoonMemberJpaRepository cartoonMemberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public CartoonMember save(CartoonMember cartoonMember) {
        return cartoonMemberJpaRepository.save(cartoonMember);
    }

    @Override
    public CartoonMember getById(Long cartoonMemberId) {
        return cartoonMemberJpaRepository.findById(cartoonMemberId)
                .orElseThrow(CartoonMemberNotFoundException::new);
    }

    @Override
    public Optional<CartoonMember> findByCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(cartoonMember)
                .leftJoin(cartoonMember.cartoon, cartoon)
                .fetchJoin()
                .where(cartoonMember.cartoon.id.eq(cartoonId))
                .leftJoin(cartoonMember.member, member)
                .fetchJoin()
                .where(cartoonMember.member.id.eq(memberId))
                .fetchOne());
    }

    // todo 쿼리 두방
    @Override
    public List<Cartoon> findAllCartoonByMemberId(Long memberId) {
        return jpaQueryFactory.select(cartoonMember.cartoon)
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon.author, author)
                .fetchJoin()
                .leftJoin(cartoonMember.member, member)
                .where(cartoonMember.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public long findCartoonSizeWhereRated(Long cartoonId) {
        return jpaQueryFactory.select(cartoonMember.count())
                .from(cartoonMember)
                .where(cartoonMember.cartoon.id.eq(cartoonId))
                .where(cartoonMember.rated.eq(true))
                .fetchOne().longValue();
    }

    @Override
    public List<Cartoon> findLikeListForMember(Long memberId) {
        return jpaQueryFactory.select(cartoonMember.cartoon)
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon.author, author)
                .fetchJoin()
                .where(cartoonMember.member.id.eq(memberId))
                .where(cartoonMember.thumbsUp.eq(true))
                .orderBy(cartoonMember.id.desc())
                .fetch();
    }

    @Override
    public List<Cartoon> findAllByMemberAge(CartoonSearch cartoonSearch) {
        jpaQueryFactory.select(cartoonMember)
                .leftJoin(cartoonMember.member, member)
                .fetchJoin()
                .leftJoin(cartoonMember.cartoon, cartoon)
                .fetchJoin()
                .leftJoin(cartoon.author, author)
                .fetchJoin()
                .where(cartoonMember.member.birthDate.before(
                        LocalDate.now().minusYears(cartoonSearch.getAgeRange() - 1)))
                .where(cartoonMember.member.birthDate.after(
                        LocalDate.now().minusYears(cartoonSearch.getAgeRange() + 8)))
                .where(cartoonMember.thumbsUp.eq(true))
                .fetch();

        return null;
    }

    @Override
    public void deleteAll() {
        cartoonMemberJpaRepository.deleteAll();
    }

    @Override
    public long count() {
        return cartoonMemberJpaRepository.count();
    }
}
