package com.webtoon.cartoonmember.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.cartoonmember.domain.CartoonMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.webtoon.cartoon.domain.QCartoon.cartoon;
import static com.webtoon.cartoonmember.domain.QCartoonMember.cartoonMember;
import static com.webtoon.member.domain.QMember.member;


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
                .leftJoin(cartoonMember.cartoon, cartoon)
                .fetchJoin()
                .where(cartoonMember.cartoon.id.eq(cartoonId))
                .leftJoin(cartoonMember.member, member)
                .fetchJoin()
                .where(cartoonMember.member.id.eq(memberId))
                .fetchOne());
    }
}
