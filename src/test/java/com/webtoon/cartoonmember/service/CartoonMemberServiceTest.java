package com.webtoon.cartoonmember.service;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class CartoonMemberServiceTest extends ServiceTest {

    @Autowired
    private CartoonMemberService cartoonMemberService;

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
        cartoonMemberService.save(cartoonMemberSave);

        // then
        assertThat(cartoonMemberRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("만화 아이디, 회원 아이디로 만화 - 회원 연결 관계를 찾습니다")
    void getByCartoonIdAndMemberId200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = saveCartoonMemberInRepository(cartoon, member);

        // when
        CartoonMember findCartoonMember =
                cartoonMemberService.getByCartoonIdAndMemberId(cartoon.getId(), member.getId());

        // then
        assertThat(cartoonMember).isEqualTo(findCartoonMember);
    }

    @Test
    @DisplayName("만화 - 회원 연결관계가 없다면 예외가 발생합니다")
    void getByCartoonIdAndMemberId404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        // expected
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberService.getByCartoonIdAndMemberId(cartoon.getId(), member.getId()));
    }

    @Test
    @DisplayName("좋아요를 누르면 CartoonMember의 thumbsUp 필드가 true로 변경됩니다")
    void thumbsUp() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = saveCartoonMemberInRepository(cartoon, member);

        // when
        cartoonMemberService.thumbsUp(cartoon.getId(), member.getId());

        // then
        assertThat(cartoonMember.getThumbsUp()).isEqualTo(TRUE);
    }

    @Test
    @DisplayName("좋아요를 누르면 Cartoon의 좋아요 개수가 1 증가합니다")
    void addLike() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        // when
        cartoonMemberService.addLike(cartoon.getId());

        // then
        assertThat(cartoon.getLikes()).isEqualTo(1);
    }

    @Test
    void findAllForMember() {
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
        CartoonMember cartoonMember = saveCartoonMemberInRepository(cartoon, member);
        cartoonMember.thumbsUp();

        // when
        List<Cartoon> cartoonList = cartoonMemberService.findLikeListForMember(member.getId());

        // then
        assertThat(cartoonList.size()).isEqualTo(1);
    }
}