package com.webtoon.cartoonmember.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.author.domain.QAuthor;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.domain.QCartoon;
import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoon.dto.response.QCartoonCore;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.domain.QCartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonSearchAge;
import com.webtoon.cartoonmember.dto.request.CartoonSearchGender;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;
import com.webtoon.cartoonmember.dto.response.QCartoonMemberResponse;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.member.domain.QMember;
import com.webtoon.util.constant.ConstantCommon;
import com.webtoon.util.enumerated.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.webtoon.author.domain.QAuthor.author;
import static com.webtoon.cartoon.domain.QCartoon.*;
import static com.webtoon.cartoonmember.domain.QCartoonMember.cartoonMember;
import static com.webtoon.member.domain.QMember.member;
import static com.webtoon.util.constant.ConstantCommon.*;


@RequiredArgsConstructor
@Repository
public class CartoonMemberRepositoryImpl implements CartoonMemberRepository {

    private final CartoonMemberJpaRepository cartoonMemberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<CartoonMember> findByCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(cartoonMember)
                        .leftJoin(cartoonMember.cartoon, cartoon)
                        .fetchJoin()
                        .where(cartoon.id.eq(cartoonId))
                        .leftJoin(cartoonMember.member, member)
                        .fetchJoin()
                        .where(member.id.eq(memberId))
                        .fetchOne());
    }

    @Override
    public List<CartoonMemberResponse> findAllCartoonByMemberId(Long memberId) {
        return jpaQueryFactory
                .select(new QCartoonMemberResponse(
                        cartoon.title,
                        author.nickname,
                        cartoonMember.lastReadDate
                ))
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon, cartoon)
                .leftJoin(cartoon.author, author)
                .leftJoin(cartoonMember.member, member)
                .where(member.id.eq(memberId))
                .orderBy(cartoonMember.lastReadDate.desc())
                .fetch();
    }

    @Override
    public List<CartoonMemberResponse> findLikeListForMember(Long memberId) {
        return jpaQueryFactory
                .select(new QCartoonMemberResponse(
                        cartoon.title,
                        author.nickname,
                        cartoonMember.lastReadDate
                ))
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon, cartoon)
                .leftJoin(cartoon.author, author)
                .leftJoin(cartoonMember.member, member)
                .where(
                        cartoonMember.member.id.eq(memberId),
                        cartoonMember.thumbsUp.eq(true)
                )
                .orderBy(cartoonMember.lastReadDate.desc())
                .fetch();
    }

    @Override
    public long findCartoonSizeWhereRated(Long cartoonId) {
        return jpaQueryFactory.select(cartoonMember.count())
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon, cartoon)
                .where(
                        cartoon.id.eq(cartoonId),
                        cartoonMember.rated.eq(true)
                )
                .fetchOne().longValue();
    }

    @Override
    public List<CartoonCore> findAllByMemberAge(CartoonSearchAge cartoonSearchAge) {
        return jpaQueryFactory
                .select(new QCartoonCore(
                        cartoon.title,
                        author.nickname,
                        cartoon.likes
                ))
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon, cartoon)
                .leftJoin(cartoon.author, author)
                .leftJoin(cartoonMember.member, member)
                .where(
                        member.birthDate.between(
                                LocalDate.now().minusYears(lowerBoundary(cartoonSearchAge.getAgeRange())),
                                LocalDate.now().minusYears(upperBoundary(cartoonSearchAge.getAgeRange())))
                )
                .groupBy(cartoon.title)
                .orderBy(cartoon.likes.desc())
                .offset(cartoonSearchAge.getPage())
                .limit(cartoonSearchAge.getSize())
                .fetch();
    }

    @Override
    public List<CartoonCore> findAllByMemberGender(CartoonSearchGender cartoonSearchGender) {
        return jpaQueryFactory.select(new QCartoonCore(
                        cartoon.title,
                        author.nickname,
                        cartoon.likes
                ))
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon, cartoon)
                .leftJoin(cartoon.author, author)
                .leftJoin(cartoonMember.member, member)
                .where(
                        member.gender.eq(Gender.valueOf(cartoonSearchGender.getGender()))
                )
                .groupBy(cartoon.title)
                .orderBy(cartoonMember.cartoon.count().desc())
                .offset(cartoonSearchGender.getPage())
                .limit(cartoonSearchGender.getSize())
                .fetch();
    }

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
    public void deleteAll() {
        cartoonMemberJpaRepository.deleteAll();
    }

    @Override
    public long count() {
        return cartoonMemberJpaRepository.count();
    }
}
