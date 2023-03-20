package com.webtoon.cartoonmember.application;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.*;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import com.webtoon.util.enumerated.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonMemberService {

    private final CartoonMemberRepository cartoonMemberRepository;
    private final CartoonRepository cartoonRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(CartoonMemberSave cartoonMemberSave) {
        Cartoon cartoon = cartoonRepository.getById(cartoonMemberSave.getCartoonId());
        Member member = memberRepository.getById(cartoonMemberSave.getMemberId());
        CartoonMember cartoonMember = CartoonMember.getFromCartoonAndMember(cartoon, member);
        if (!validateAlreadyRead(cartoonMemberSave.getCartoonId(), cartoonMemberSave.getMemberId())) {
            cartoonMemberRepository.save(cartoonMember);
        }
        cartoonMember.updateReadDate(LocalDateTime.now());
    }


    @Transactional
    public void thumbsUp(CartoonMemberThumbsUp cartoonMemberThumbsUp) {
        CartoonMember cartoonMember = cartoonMemberRepository.findByCartoonIdAndMemberId(
                        cartoonMemberThumbsUp.getCartoonId(), cartoonMemberThumbsUp.getMemberId())
                .orElseThrow(CartoonMemberNotFoundException::new);

        cartoonMember.thumbsUp();
        Cartoon cartoon = cartoonMember.getCartoon();
        cartoon.addLike();
    }

    @Transactional
    public void rating(CartoonMemberRating cartoonMemberRating) {
        CartoonMember cartoonMember = cartoonMemberRepository.findByCartoonIdAndMemberId(
                        cartoonMemberRating.getCartoonId(), cartoonMemberRating.getMemberId())
                .orElseThrow(CartoonMemberNotFoundException::new);

        if (!cartoonMember.isRated()) {
            long cartoonListSize = cartoonMemberRepository.findCartoonSizeWhereRated(cartoonMemberRating.getCartoonId());
            Cartoon cartoon = cartoonMember.getCartoon();
            cartoon.rating(cartoonMemberRating.getRating(), cartoonListSize);
            cartoonMember.rated();
        }
    }

    public List<CartoonMemberResponse> findAllCartoonByMemberId(Long memberId) {
        return cartoonMemberRepository.findAllCartoonByMemberId(memberId);
    }

    public List<CartoonMemberResponse> findLikeListForMember(Long memberId) {
        return cartoonMemberRepository.findLikeListForMember(memberId);
    }

    public List<CartoonCore> findAllByMemberAge(CartoonSearchAge cartoonSearchAge) {
        return cartoonMemberRepository.findAllByMemberAge(cartoonSearchAge);
    }

    public List<CartoonCore> findAllByMemberGender(CartoonSearchGender cartoonSearchGender) {
        Gender.validateValid(cartoonSearchGender.getGender());
        return cartoonMemberRepository.findAllByMemberGender(cartoonSearchGender);
    }

    public boolean validateAlreadyRead(Long cartoonId, Long memberId) {
        Optional<CartoonMember> cartoonMember = cartoonMemberRepository.findByCartoonIdAndMemberId(cartoonId, memberId);
        return cartoonMember.isPresent();
    }
}
