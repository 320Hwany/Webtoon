package com.webtoon.cartoonmember.repository;


import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;

import java.util.List;
import java.util.Optional;

public interface CartoonMemberRepository {

    CartoonMember save(CartoonMember cartoonMember);

    CartoonMember getById(Long cartoonMemberId);

    Optional<CartoonMember> findByCartoonIdAndMemberId(Long cartoonId, Long memberId);

    List<CartoonMemberResponse> findAllCartoonByMemberId(Long memberId);

    List<CartoonMemberResponse> findLikeListForMember(Long memberId);

    long findCartoonSizeWhereRated(Long cartoonId);

    List<CartoonCore> findAllByMemberAge(CartoonSearch cartoonSearch);

    void deleteAll();

    long count();
}
