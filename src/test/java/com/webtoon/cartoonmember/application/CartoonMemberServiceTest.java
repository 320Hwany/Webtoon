package com.webtoon.cartoonmember.application;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.*;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.member.domain.Member;
import com.webtoon.member.exception.MemberNotFoundException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Gender;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.webtoon.cartoonmember.dto.request.CartoonMemberThumbsUp.*;
import static com.webtoon.util.constant.ConstantCommon.ZERO_OF_TYPE_DOUBLE;
import static com.webtoon.util.constant.ConstantCommon.ZERO_OF_TYPE_LONG;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonMemberServiceTest extends ServiceTest {

    @Autowired
    private CartoonMemberService cartoonMemberService;

    @Test
    @DisplayName("처음읽는 만화라면 만화와 회원정보로부터 연결 정보를 저장합니다")
    void save200() {
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
        assertThat(cartoonMemberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("cartoonId, memberId 각각과 일치하는 만화, 회원이 없다면 예외가 발생합니다")
    void save404() {
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
                () -> cartoonMemberService
                        .save(cartoonMemberSaveWithoutCartoon));
        assertThrows(MemberNotFoundException.class,
                () -> cartoonMemberService
                        .save(cartoonMemberSaveWithoutMember));
    }

    @Test
    @DisplayName("처음읽는 만화라면 만화와 회원정보로부터 연결 정보를 저장합니다")
    void readCartoon() {
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
                .build();

        // when
        cartoonMemberService.readCartoon(cartoonMemberSave, cartoonMember);

        // then
        assertThat(cartoonMemberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("처음읽는 만화가 아니라면 CartoonMember의 마지막으로 읽은 날짜만 업데이트합니다")
    void alreadyReadCartoon() {
        // given 1
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // given 2
        CartoonMemberSave cartoonMemberSave = CartoonMemberSave.builder()
                .cartoonId(cartoon.getId())
                .memberId(member.getId())
                .build();

        // when
        cartoonMemberService.readCartoon(cartoonMemberSave, cartoonMember);

        // then
        CartoonMember findCartoonMember = cartoonMemberRepository.getById(cartoonMember.getId());

        assertThat(cartoonMemberRepository.count()).isEqualTo(1);
        assertThat(findCartoonMember.getLastReadDate()).isNotEqualTo(cartoonMember.getLastReadDate());
    }

    @Test
    @DisplayName("이미 읽은 만화이면 true를 반환합니다")
    void validateAlreadyRead_true() {
        // given 1
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // given 2
        CartoonMemberSave cartoonMemberSave = CartoonMemberSave.builder()
                .cartoonId(cartoon.getId())
                .memberId(member.getId())
                .build();

        // when
        boolean validateAlreadyRead = cartoonMemberService.validateAlreadyRead(cartoonMemberSave);

        // then
        assertThat(validateAlreadyRead).isTrue();
    }

    @Test
    @DisplayName("이미 읽은 만화이면 false 반환합니다")
    void validateAlreadyRead_false() {
        // given 1
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();

        // given 2
        CartoonMemberSave cartoonMemberSave = CartoonMemberSave.builder()
                .cartoonId(cartoon.getId())
                .memberId(member.getId())
                .build();

        // when
        boolean validateAlreadyRead = cartoonMemberService.validateAlreadyRead(cartoonMemberSave);

        // then
        assertThat(validateAlreadyRead).isFalse();
    }

    @Test
    @DisplayName("회원이 만화를 좋아요를 누르면 만화는 좋아요 수가 올라가고 연결 테이블에는 thumbsUp 필드가 true가 됩니다")
    void thumbsUp200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = saveCartoonMemberInRepository(cartoon, member);
        CartoonMemberThumbsUp cartoonMemberThumbsUp = toCartoonMemberThumbsUp(cartoon.getId(), member.getId());

        // when
        cartoonMemberService.thumbsUp(cartoonMemberThumbsUp);

        // then
        Cartoon findCartoon = cartoonRepository.getById(cartoon.getId());
        CartoonMember findCartoonMember = cartoonMemberRepository.getById(cartoonMember.getId());

        assertThat(findCartoon.getLikes()).isEqualTo(1);
        assertThat(findCartoonMember.isThumbsUp()).isEqualTo(true);
    }

    @Test
    @DisplayName("memberId, cartoonId로 회원 - 만화 연결 테이블에 있는 정보를 찾을 수 없으면 예외가 발생합니다")
    void thumbsUp404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);

        CartoonMemberThumbsUp cartoonMemberWithoutCartoon = toCartoonMemberThumbsUp(9999L, member.getId());
        CartoonMemberThumbsUp cartoonMemberWithoutMember = toCartoonMemberThumbsUp(cartoon.getId(), 9999L);
        CartoonMemberThumbsUp cartoonMemberWithoutBoth = toCartoonMemberThumbsUp(9999L, 9999L);

        // expected
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberService.thumbsUp(cartoonMemberWithoutCartoon));
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberService.thumbsUp(cartoonMemberWithoutMember));
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberService.thumbsUp(cartoonMemberWithoutBoth));
    }

    @Test
    void rating200() {
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
                CartoonMemberRating.toCartoonMemberRating(cartoon.getId(), member.getId(), 9.8);

        // when
        cartoonMemberService.rating(cartoonMemberRating);

        // then
        Cartoon findCartoon = cartoonRepository.getById(cartoon.getId());
        assertThat(findCartoon.getRating()).isEqualTo(9.8);
    }

    @Test
    void rating404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);

        CartoonMemberRating cartoonMemberWithoutCartoon =
                CartoonMemberRating.toCartoonMemberRating(9999L, member.getId(), 9.8);
        CartoonMemberRating cartoonMemberWithoutMember =
                CartoonMemberRating.toCartoonMemberRating(cartoon.getId(), 9999L, 9.8);
        CartoonMemberRating cartoonMemberWithoutBoth =
                CartoonMemberRating.toCartoonMemberRating(9999L, 9999L, 9.8);

        // expected
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberService.rating(cartoonMemberWithoutCartoon));
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberService.rating(cartoonMemberWithoutMember));
        assertThrows(CartoonMemberNotFoundException.class,
                () -> cartoonMemberService.rating(cartoonMemberWithoutBoth));
    }

    @Test
    void findAllCartoonByMemberId() {
        // given 1
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = saveCartoonMemberInRepository(cartoon, member);

        // given 2 - another member
        Member anotherMember = Member.builder()
                .nickname("다른 회원")
                .email("anotherMember@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(ZERO_OF_TYPE_LONG)
                .birthDate(LocalDate.of(1999,03,20))
                .gender(Gender.MAN)
                .build();

        memberRepository.save(anotherMember);

        // when
        List<CartoonMemberResponse> cartoonMemberResponseList =
                cartoonMemberService.findAllCartoonByMemberId(member.getId());

        // then
        assertThat(cartoonMemberResponseList.size()).isEqualTo(1);
        assertThat(cartoonMemberResponseList.get(0).getTitle()).isEqualTo(cartoon.getTitle());
        assertThat(cartoonMemberResponseList.get(0).getAuthorNickname()).isEqualTo(author.getNickname());
        assertThat(cartoonMemberResponseList.get(0).getLastReadDate()).isEqualTo(cartoonMember.getLastReadDate());
    }

    @Test
    void findLikeListForMember() {
        // given 1
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(true)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // given 2 - another cartoon
        Cartoon anotherCartoon = Cartoon.builder()
                .title("another cartoon")
                .author(author)
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .rating(ZERO_OF_TYPE_DOUBLE)
                .likes(ZERO_OF_TYPE_LONG)
                .build();

        cartoonRepository.save(anotherCartoon);

        CartoonMember anotherCartoonMember = CartoonMember.builder()
                .cartoon(anotherCartoon)
                .member(member)
                .thumbsUp(false)
                .build();

        cartoonMemberRepository.save(anotherCartoonMember);

        // when
        List<CartoonMemberResponse> cartoonMemberResponseList
                = cartoonMemberService.findLikeListForMember(member.getId());

        // then
        assertThat(cartoonMemberResponseList.size()).isEqualTo(1);
        assertThat(cartoonMemberResponseList.get(0).getTitle()).isEqualTo(cartoon.getTitle());
        assertThat(cartoonMemberResponseList.get(0).getAuthorNickname()).isEqualTo(author.getNickname());
        assertThat(cartoonMemberResponseList.get(0).getLastReadDate()).isEqualTo(cartoonMember.getLastReadDate());
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

        CartoonSearchAge cartoonSearchAge = CartoonSearchAge.builder()
                .page(1)
                .size(10)
                .ageRange(20)
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // given 2 another member
        Member anotherMember = Member.builder()
                .nickname("다른 회원 닉네임")
                .email("anotherMember@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(ZERO_OF_TYPE_LONG)
                .birthDate(LocalDate.of(2009,03,20))
                .gender(Gender.MAN)
                .build();

        memberRepository.save(anotherMember);

        CartoonMember anotherCartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(true)
                .rated(true)
                .build();

        cartoonMemberRepository.save(anotherCartoonMember);

        // when
        List<CartoonCore> cartoonCoreList = cartoonMemberService.findAllByMemberAge(cartoonSearchAge);

        // then
        assertThat(cartoonCoreList.size()).isEqualTo(1);
        assertThat(cartoonCoreList.get(0).getTitle()).isEqualTo(cartoon.getTitle());
        assertThat(cartoonCoreList.get(0).getNickname()).isEqualTo(author.getNickname());
        assertThat(cartoonCoreList.get(0).getLikes()).isEqualTo(cartoon.getLikes());
    }

    @Test
    void findAllByMemberGender() {
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

        CartoonSearchGender cartoonSearchGender = CartoonSearchGender.builder()
                .page(1)
                .size(10)
                .gender("MAN")
                .build();

        cartoonMemberRepository.save(cartoonMember);

        // given 2 another member
        Member anotherMember = Member.builder()
                .nickname("다른 회원 닉네임")
                .email("anotherMember@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(ZERO_OF_TYPE_LONG)
                .birthDate(LocalDate.of(2009,03,20))
                .gender(Gender.WOMAN)
                .build();

        memberRepository.save(anotherMember);

        CartoonMember anotherCartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(true)
                .rated(true)
                .build();

        cartoonMemberRepository.save(anotherCartoonMember);

        // when
        List<CartoonCore> cartoonCoreList = cartoonMemberService.findAllByMemberGender(cartoonSearchGender);

        // then
        assertThat(cartoonCoreList.size()).isEqualTo(1);
        assertThat(cartoonCoreList.get(0).getTitle()).isEqualTo(cartoon.getTitle());
        assertThat(cartoonCoreList.get(0).getNickname()).isEqualTo(author.getNickname());
        assertThat(cartoonCoreList.get(0).getLikes()).isEqualTo(cartoon.getLikes());
    }
}