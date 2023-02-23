package com.webtoon.cartoonmember.application;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberRating;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.dto.request.CartoonMemberThumbsUp;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.member.domain.Member;
import com.webtoon.member.exception.MemberNotFoundException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
class CartoonMemberTransactionalServiceTest extends ServiceTest {

    @Autowired
    private CartoonMemberTransactionalService cartoonMemberTransactionalService;

    @Test
    @DisplayName("처음읽는 만화라면 CartoonMemberSave 정보로부터 CartoonMember를 저장합니다")
    void saveSet200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        CartoonMemberSave cartoonMemberSave = CartoonMemberSave.builder()
                .cartoonId(cartoon.getId())
                .memberId(member.getId())
                .build();

        // when
        cartoonMemberTransactionalService.saveSet(cartoonMemberSave);

        // then
        assertThat(cartoonMemberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("처음읽는 만화가 아니라면 CartoonMember의 마지막으로 읽은 날짜만 업데이트합니다")
    void saveSetAlreadyRead() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        CartoonMemberSave cartoonMemberSave = CartoonMemberSave.builder()
                .cartoonId(cartoon.getId())
                .memberId(member.getId())
                .build();

        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(true)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // when
        cartoonMemberTransactionalService.saveSet(cartoonMemberSave);

        // then
        assertThat(cartoonMemberRepository.count()).isEqualTo(1);
        assertThat(cartoonMember.getLastModifiedDateTime()).isNotNull();
    }

    @Test
    @DisplayName("cartoonId, memberId 각각과 일치하는 만화, 회원이 없다면 예외가 발생합니다")
    void saveSet404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        CartoonMemberSave cartoonMemberSaveWithoutCartoon = CartoonMemberSave.builder()
                .cartoonId(9999L)
                .memberId(member.getId())
                .build();

        CartoonMemberSave cartoonMemberSaveWithoutMember = CartoonMemberSave.builder()
                .cartoonId(cartoon.getId())
                .memberId(9999L)
                .build();

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonMemberTransactionalService
                        .saveSet(cartoonMemberSaveWithoutCartoon));
        assertThrows(MemberNotFoundException.class,
                () -> cartoonMemberTransactionalService
                        .saveSet(cartoonMemberSaveWithoutMember));
    }

    @Test
    @DisplayName("회원이 만화를 좋아요를 누르면 만화는 좋아요 수가 올라가고 연결 테이블에는 thumbsUp이 true가 됩니다")
    void thumbsUpTransactionSet200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = saveCartoonMemberInRepository(cartoon, member);
        CartoonMemberThumbsUp cartoonMemberThumbsUp =
                CartoonMemberThumbsUp.getFromCartoonIdAndMemberId(cartoon.getId(), member.getId());

        // when
        cartoonMemberTransactionalService.thumbsUpTransactionSet(cartoonMemberThumbsUp);

        // then
        Cartoon findCartoon = cartoonRepository.getById(cartoon.getId());
        CartoonMember findCartoonMember = cartoonMemberRepository.getById(cartoonMember.getId());

        assertThat(findCartoon.getLikes()).isEqualTo(1);
        assertThat(findCartoonMember.isThumbsUp()).isEqualTo(true);
    }

    @Test
    @DisplayName("memberId, cartoonId로 회원 - 만화 연결 테이블에 있는 정보를 찾을 수 없으면 예외가 발생합니다")
    void thumbsUpTransactionSet404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);

        CartoonMemberThumbsUp cartoonMemberWithoutCartoon =
                CartoonMemberThumbsUp.getFromCartoonIdAndMemberId(9999L, member.getId());
        CartoonMemberThumbsUp cartoonMemberWithoutMember =
                CartoonMemberThumbsUp.getFromCartoonIdAndMemberId(cartoon.getId(), 9999L);
        CartoonMemberThumbsUp cartoonMemberWithoutBoth =
                CartoonMemberThumbsUp.getFromCartoonIdAndMemberId(9999L, 9999L);

        // expected
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberTransactionalService.thumbsUpTransactionSet(cartoonMemberWithoutCartoon));
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberTransactionalService.thumbsUpTransactionSet(cartoonMemberWithoutMember));
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberTransactionalService.thumbsUpTransactionSet(cartoonMemberWithoutBoth));
    }

    @Test
    void ratingTransactionSet200() {
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

        CartoonMemberRating cartoonMemberRating =
                CartoonMemberRating.getFromIdAndRating(cartoon.getId(), member.getId(), 9.8);

        // when
        cartoonMemberTransactionalService.ratingTransactionSet(cartoonMemberRating);

        // then
        Cartoon findCartoon = cartoonRepository.getById(cartoon.getId());
        assertThat(findCartoon.getRating()).isEqualTo(9.8);
    }

    @Test
    void ratingTransactionSet404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);

        CartoonMemberRating cartoonMemberWithoutCartoon =
                CartoonMemberRating.getFromIdAndRating(9999L, member.getId(), 9.8);
        CartoonMemberRating cartoonMemberWithoutMember =
                CartoonMemberRating.getFromIdAndRating(cartoon.getId(), 9999L, 9.8);
        CartoonMemberRating cartoonMemberWithoutBoth =
                CartoonMemberRating.getFromIdAndRating(9999L, 9999L, 9.8);

        // expected
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberTransactionalService.ratingTransactionSet(cartoonMemberWithoutCartoon));
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberTransactionalService.ratingTransactionSet(cartoonMemberWithoutMember));
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberTransactionalService.ratingTransactionSet(cartoonMemberWithoutBoth));
    }
}