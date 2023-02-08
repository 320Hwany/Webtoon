package com.webtoon.cartoonmember.service;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


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
        cartoonMemberRepository.save(cartoonMember);
    }

    @Transactional
    public void thumbsUp(Long cartoonId, Long memberId) {
        CartoonMember cartonMember = getByCartoonIdAndMemberId(cartoonId, memberId);
        cartonMember.thumbsUp();
    }

    public CartoonMember getByCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        CartoonMember cartoonMember = cartoonMemberRepository.findByCartoonIdAndMemberId(cartoonId, memberId)
                .orElseThrow(CartoonMemberNotFoundException::new);
        return cartoonMember;
    }

    @Transactional
    public void addLike(Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.addLike();
    }

    @Transactional
    public Boolean isRated(CartoonMember cartoonMember) {
        if(cartoonMember.getRated() == FALSE){
            cartoonMember.isRated();
            return true;
        }
        return false;
    }

    public List<Cartoon> findAllCartoonByMemberId(Long memberId) {
        return cartoonMemberRepository.findAllCartoonByMemberId(memberId);
    }

    public int findAllCartoonByCartoonIdWhereRated(Long cartoonId) {
        return cartoonMemberRepository.findAllCartoonSizeByCartoonIdWhereRated(cartoonId);
    }

    public List<Cartoon> findLikeListForMember(Long memberId) {
        return cartoonMemberRepository.findLikeListForMember(memberId);
    }
}
