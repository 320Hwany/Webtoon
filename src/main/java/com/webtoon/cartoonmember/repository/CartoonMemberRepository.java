package com.webtoon.cartoonmember.repository;


import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CartoonMemberRepository {

    CartoonMember save(CartoonMember cartoonMember);

    CartoonMember getById(Long cartoonMemberId);

    Optional<CartoonMember> findByCartoonIdAndMemberId(Long cartoonId, Long memberId);

    List<Cartoon> findAllCartoonByMemberId(Long memberId);

    long findCartoonSizeWhereRated(Long cartoonId);

    List<Cartoon> findLikeListForMember(Long memberId);

    List<Cartoon> findAllByMemberAge(CartoonSearch cartoonSearch);

    void deleteAll();

    long count();
}
