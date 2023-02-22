package com.webtoon.cartoonmember.application;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberRating;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.dto.request.CartoonMemberThumbsUp;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonMemberTransactionalService {

    private final CartoonMemberRepository cartoonMemberRepository;
    private final CartoonRepository cartoonRepository;
    private final MemberRepository memberRepository;
    private final CartoonMemberService cartoonMemberService;

    @Transactional
    public void saveSet(CartoonMemberSave cartoonMemberSave, boolean alreadyRead) {
        Cartoon cartoon = cartoonRepository.getById(cartoonMemberSave.getCartoonId());
        Member member = memberRepository.getById(cartoonMemberSave.getMemberId());
        CartoonMember cartoonMember = CartoonMember.getFromCartoonAndMember(cartoon, member);
        if (!cartoonMemberService.validateAlreadyRead(cartoonMemberSave.getCartoonId(), cartoonMemberSave.getMemberId())) {
            cartoonMemberRepository.save(cartoonMember);
        }
        cartoonMember.updateReadDate(LocalDateTime.now());
    }

    @Transactional
    public void thumbsUpTransactionSet(CartoonMemberThumbsUp cartoonMemberThumbsUp) {
        CartoonMember cartoonMember = cartoonMemberRepository.findByCartoonIdAndMemberId(
                cartoonMemberThumbsUp.getCartoonId(),
                cartoonMemberThumbsUp.getMemberId()).orElseThrow(CartoonMemberNotFoundException::new);
        cartoonMember.thumbsUp();
        Cartoon cartoon = cartoonMember.getCartoon();
        cartoon.addLike();
    }

    @Transactional
    public void ratingTransactionSet(CartoonMemberRating cartoonMemberRating) {
        CartoonMember cartoonMember = cartoonMemberRepository
                .findByCartoonIdAndMemberId(cartoonMemberRating.getCartoonId(), cartoonMemberRating.getMemberId())
                .orElseThrow(CartoonMemberNotFoundException::new);

        if (cartoonMember.isRated() == false) {
            long cartoonListSize = cartoonMemberRepository
                    .findCartoonSizeWhereRated(cartoonMemberRating.getCartoonId());
            Cartoon cartoon = cartoonMember.getCartoon();
            cartoon.rating(cartoonMemberRating.getRating(), cartoonListSize);
            cartoonMember.rated();
        }
    }
}
