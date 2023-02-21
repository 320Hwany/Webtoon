package com.webtoon.cartoonmember.application;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonMemberService {

    private final CartoonMemberRepository cartoonMemberRepository;

    public List<Cartoon> findAllCartoonByMemberId(Long memberId) {
        return cartoonMemberRepository.findAllCartoonByMemberId(memberId);
    }

    public List<Cartoon> findLikeListForMember(Long memberId) {
        return cartoonMemberRepository.findLikeListForMember(memberId);
    }

    public List<Cartoon> findAllByMemberAge(CartoonSearch cartoonSearch) {
        return cartoonMemberRepository.findAllByMemberAge(cartoonSearch);
    }
}
