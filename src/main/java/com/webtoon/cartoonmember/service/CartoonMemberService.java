package com.webtoon.cartoonmember.service;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonMemberService {

    private final CartoonMemberRepository cartoonMemberRepository;
    private final CartoonRepository cartoonRepository;
    private final MemberRepository memberRepository;

    public CartoonMemberSave getCartoonMemberSaveFromId(Long cartoonId, Long memberId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        Member member = memberRepository.getById(memberId);
        return CartoonMemberSave.getFromCartoonAndMember(cartoon, member);
    }

    @Transactional
    public void save(CartoonMemberSave cartoonMemberSave) {
        CartoonMember cartoonMember = CartoonMember.getFromCartoonMemberSave(cartoonMemberSave);
        cartoonMemberRepository.save(cartoonMember);
    }
}
