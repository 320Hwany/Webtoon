package com.webtoon.cartoonmember.application;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonMemberService {

    private final CartoonMemberRepository cartoonMemberRepository;

    public List<CartoonMemberResponse> findAllCartoonByMemberId(Long memberId) {
        return cartoonMemberRepository.findAllCartoonByMemberId(memberId);
    }

    public List<CartoonMemberResponse> findLikeListForMember(Long memberId) {
        return cartoonMemberRepository.findLikeListForMember(memberId);
    }

    public List<CartoonCore> findAllByMemberAge(CartoonSearch cartoonSearch) {
        return cartoonMemberRepository.findAllByMemberAge(cartoonSearch);
    }

    public CartoonMember findByCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        return cartoonMemberRepository.findByCartoonIdAndMemberId(cartoonId, memberId)
                .orElseThrow(CartoonMemberNotFoundException::new);
    }

    public long findCartoonSizeWhereRated(Long cartoonId) {
        return cartoonMemberRepository.findCartoonSizeWhereRated(cartoonId);
    }

    public boolean validateAlreadyRead(Long cartoonId, Long memberId) {
        Optional<CartoonMember> cartoonMember = cartoonMemberRepository.findByCartoonIdAndMemberId(cartoonId, memberId);
        return cartoonMember.isPresent();
    }
}
