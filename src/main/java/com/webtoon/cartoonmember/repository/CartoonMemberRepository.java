package com.webtoon.cartoonmember.repository;


import com.webtoon.cartoonmember.domain.CartoonMember;

import java.util.List;
import java.util.Optional;

public interface CartoonMemberRepository {

    CartoonMember save(CartoonMember cartoonMember);

    Optional<CartoonMember> findByCartoonIdAndMemberId(Long cartoonId, Long memberId);

    long count();
}
