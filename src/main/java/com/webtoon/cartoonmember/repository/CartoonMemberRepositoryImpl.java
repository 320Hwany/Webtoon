package com.webtoon.cartoonmember.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.QCartoon;
import com.webtoon.cartoonmember.domain.CartoonMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.webtoon.author.domain.QAuthor.author;
import static com.webtoon.cartoonmember.domain.QCartoonMember.cartoonMember;
import static com.webtoon.member.domain.QMember.member;
import static java.lang.Boolean.TRUE;


@RequiredArgsConstructor
@Repository
public class CartoonMemberRepositoryImpl implements CartoonMemberRepository {

    private final CartoonMemberJpaRepository cartoonMemberJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CartoonMember save(CartoonMember cartoonMember) {
        return cartoonMemberJpaRepository.save(cartoonMember);
    }

    @Override
    public Optional<CartoonMember> findByCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(cartoonMember)
                .leftJoin(cartoonMember.cartoon, QCartoon.cartoon)
                .fetchJoin()
                .where(cartoonMember.cartoon.id.eq(cartoonId))
                .leftJoin(cartoonMember.member, member)
                .fetchJoin()
                .where(cartoonMember.member.id.eq(memberId))
                .fetchOne());
    }

    @Override
    public List<Cartoon> findAllCartoonByMemberId(Long memberId) {
        return jpaQueryFactory.select(cartoonMember.cartoon)
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon.author, author)
                .fetchJoin()
                .where(cartoonMember.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Cartoon> findAllCartoonByCartoonId(Long cartoonId) {
        return jpaQueryFactory.select(cartoonMember.cartoon)
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon.author, author)
                .fetchJoin()
                .where(cartoonMember.cartoon.id.eq(cartoonId))
                .fetch();
    }

    @Override
    public List<Cartoon> findLikeListForMember(Long memberId) {
        return jpaQueryFactory.select(cartoonMember.cartoon)
                .from(cartoonMember)
                .leftJoin(cartoonMember.cartoon.author, author)
                .fetchJoin()
                .where(cartoonMember.member.id.eq(memberId))
                .where(cartoonMember.thumbsUp.eq(TRUE))
                .orderBy(cartoonMember.id.desc())
                .fetch();
    }

    @Override
    public long count() {
        return cartoonMemberJpaRepository.count();
    }
}
