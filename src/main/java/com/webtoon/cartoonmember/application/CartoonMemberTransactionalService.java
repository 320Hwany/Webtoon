package com.webtoon.cartoonmember.application;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberRating;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.dto.request.CartoonMemberThumbsUp;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import com.webtoon.global.openfeign.DynamicUrlOpenFeign;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonMemberTransactionalService {

    private final CartoonMemberRepository cartoonMemberRepository;
    private final CartoonRepository cartoonRepository;
    private final MemberRepository memberRepository;
    private final CartoonMemberService cartoonMemberService;

    @Transactional
    public void saveSet(CartoonMemberSave cartoonMemberSave) {
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
        CartoonMember cartoonMember = cartoonMemberService.findByCartoonIdAndMemberId(
                cartoonMemberThumbsUp.getCartoonId(), cartoonMemberThumbsUp.getMemberId());
        cartoonMember.thumbsUp();
        Cartoon cartoon = cartoonMember.getCartoon();
        cartoon.addLike();
    }

    @Transactional
    public void ratingTransactionSet(CartoonMemberRating cartoonMemberRating) {
        CartoonMember cartoonMember = cartoonMemberService.findByCartoonIdAndMemberId(
                cartoonMemberRating.getCartoonId(), cartoonMemberRating.getMemberId());

        if (!cartoonMember.isRated()) {
            long cartoonListSize = cartoonMemberService.findCartoonSizeWhereRated(cartoonMemberRating.getCartoonId());
            Cartoon cartoon = cartoonMember.getCartoon();
            cartoon.rating(cartoonMemberRating.getRating(), cartoonListSize);
            cartoonMember.rated();
        }
    }
}
