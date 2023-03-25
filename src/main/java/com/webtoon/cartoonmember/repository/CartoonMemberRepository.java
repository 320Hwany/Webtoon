package com.webtoon.cartoonmember.repository;


import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.*;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;

import java.util.List;
import java.util.Optional;

public interface CartoonMemberRepository {

    CartoonMember save(CartoonMember cartoonMember);

    CartoonMember getById(Long cartoonMemberId);

    Optional<CartoonMember> findByCartoonMemberSave(CartoonMemberSave cartoonMemberSave);

    Optional<CartoonMember> findByCartoonMemberThumbsUp(CartoonMemberThumbsUp cartoonMemberThumbsUp);

    Optional<CartoonMember> findByCartoonMemberRating(CartoonMemberRating cartoonMemberRating);

    CartoonMember getByCartoonMemberSave(CartoonMemberSave cartoonMemberSave);

    List<CartoonMemberResponse> findAllCartoonByMemberId(Long memberId);

    List<CartoonMemberResponse> findLikeListForMember(Long memberId);

    List<CartoonCore> findAllByMemberAge(CartoonSearchAge cartoonSearchAge);

    List<CartoonCore> findAllByMemberGender(CartoonSearchGender cartoonSearchGender);

    long findCartoonSizeWhereRated(Long cartoonId);

    void deleteAll();

    long count();
}
