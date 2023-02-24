package com.webtoon.cartoonmember.application;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberRating;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.dto.request.CartoonMemberThumbsUp;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.member.domain.Member;
import com.webtoon.member.exception.MemberNotFoundException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonMemberServiceTest extends ServiceTest {

    @Autowired
    private CartoonMemberService cartoonMemberService;


    @Test
    void findAllCartoonByMemberId() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);

        // when
        List<CartoonMemberResponse> cartoonMemberResponseList =
                cartoonMemberService.findAllCartoonByMemberId(member.getId());

        // then
        assertThat(cartoonMemberResponseList.size()).isEqualTo(1);
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
        List<CartoonMemberResponse> cartoonMemberResponseList
                = cartoonMemberService.findLikeListForMember(member.getId());

        // then
        assertThat(cartoonMemberResponseList.size()).isEqualTo(1);
    }

    @Test
    void findCartoonSizeWhereRated() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(true)
                .rated(true)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // when
        long cartoonSize = cartoonMemberService.findCartoonSizeWhereRated(cartoon.getId());

        // then
        assertThat(cartoonSize).isEqualTo(1);
    }

    @Test
    void findAllByMemberAge() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(true)
                .rated(true)
                .build();

        CartoonSearch cartoonSearch = CartoonSearch.builder()
                .page(0)
                .ageRange(20)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // when
        List<CartoonCore> cartoonCoreList = cartoonMemberService.findAllByMemberAge(cartoonSearch);

        // then
        assertThat(cartoonCoreList.size()).isEqualTo(1);
    }


    @Test
    @DisplayName("이미 읽은 만화라면 true를 반환합니다")
    void validateAlreadyReadTrue() {
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
        boolean alreadyRead = cartoonMemberService.validateAlreadyRead(cartoon.getId(), member.getId());

        // then
        assertThat(alreadyRead).isEqualTo(true);
    }

    @Test
    @DisplayName("이미 읽은 만화가 아니라면 false를 반환합니다")
    void validateAlreadyReadFalse() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        // when
        boolean alreadyRead = cartoonMemberService.validateAlreadyRead(cartoon.getId(), member.getId());

        // then
        assertThat(alreadyRead).isEqualTo(false);
    }
}