package com.webtoon.cartoonmember.service;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonMemberServiceTest extends ServiceTest {

    @Autowired
    private CartoonMemberService cartoonMemberService;

    @Autowired
    private CartoonMemberTransactionalService cartoonMemberTransactionalService;


    @Test
    @DisplayName("CartoonMemberSave 정보로부터 CartoonMember를 저장합니다")
    void save() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        CartoonMemberSave cartoonMemberSave = CartoonMemberSave.builder()
                .cartoonId(cartoon.getId())
                .memberId(member.getId())
                .build();

        // when
        cartoonMemberTransactionalService.saveTransactionSet(cartoonMemberSave);

        // then
        assertThat(cartoonMemberRepository.count()).isEqualTo(1L);
    }

    @Test
    void findAllCartoonByMemberId() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);

        // when
        List<Cartoon> cartoonList = cartoonMemberService.findAllCartoonByMemberId(member.getId());

        // then
        assertThat(cartoonList.size()).isEqualTo(1);
    }

    @Test
    void findLikeListForMember() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(true)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // when
        List<Cartoon> cartoonList = cartoonMemberService.findLikeListForMember(cartoonMember.getMember().getId());

        // then
        assertThat(cartoonList.size()).isEqualTo(1);
    }

    @Test
    void ratingTransactionSet() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .rated(false)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // when
        cartoonMemberTransactionalService.ratingTransactionSet(cartoon.getId(), member.getId(), 9.8);

        // then
        Cartoon findCartoon = cartoonRepository.getById(cartoon.getId());
        assertThat(findCartoon.getRating()).isEqualTo(9.8);
    }
}