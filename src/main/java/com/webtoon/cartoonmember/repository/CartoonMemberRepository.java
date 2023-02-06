package com.webtoon.cartoonmember.repository;


import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoonmember.domain.CartoonMember;

import java.util.List;
import java.util.Optional;

public interface CartoonMemberRepository {

    CartoonMember save(CartoonMember cartoonMember);

    Optional<CartoonMember> findByCartoonIdAndMemberId(Long cartoonId, Long memberId);

    List<Cartoon> findAllCartoonByMemberId(Long memberId);

    List<Cartoon> findAllCartoonByCartoonId(Long cartoonId);

    List<Cartoon> findLikeListForMember(Long memberId);

    long count();
}
