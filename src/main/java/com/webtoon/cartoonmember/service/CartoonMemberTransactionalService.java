package com.webtoon.cartoonmember.service;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoon.service.CartoonService;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonMemberTransactionalService {

    private final CartoonMemberRepository cartoonMemberRepository;
    private final CartoonRepository cartoonRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveTransactionSet(CartoonMemberSave cartoonMemberSave) {
        Cartoon cartoon = cartoonRepository.getById(cartoonMemberSave.getCartoonId());
        Member member = memberRepository.getById(cartoonMemberSave.getMemberId());
        CartoonMember cartoonMember = CartoonMember.getFromCartoonAndMember(cartoon, member);
        cartoonMemberRepository.save(cartoonMember);
    }

    @Transactional
    public void thumbsUpTransactionSet(Long cartoonId, Long memberId) {
        CartoonMember cartoonMember = cartoonMemberRepository.findByCartoonIdAndMemberId(cartoonId, memberId)
                .orElseThrow(CartoonMemberNotFoundException::new);
        cartoonMember.thumbsUp();
        Cartoon cartoon = cartoonMember.getCartoon();
        cartoon.addLike();
    }

    @Transactional
    public void ratingTransactionSet(Long cartoonId, Long memberId, double rating) {
        CartoonMember cartoonMember = cartoonMemberRepository.findByCartoonIdAndMemberId(cartoonId, memberId)
                .orElseThrow(CartoonMemberNotFoundException::new);

        if (cartoonMember.isRated() == false) {
            long cartoonListSize = cartoonMemberRepository.findCartoonSizeWhereRated(cartoonId);
            Cartoon cartoon = cartoonMember.getCartoon();
            cartoon.rating(rating, cartoonListSize);
            cartoonMember.rated();
        }
    }
}
