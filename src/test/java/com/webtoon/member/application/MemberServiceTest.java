package com.webtoon.member.application;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.dto.response.MemberResponse;
import com.webtoon.member.exception.MemberDuplicationException;
import com.webtoon.member.exception.MemberNotFoundException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;


    @Test
    @DisplayName("회원이 저장됩니다 - 성공")
    void signup() {
        // given
        MemberSignup memberSignup = MemberSignup.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .gender("MAN")
                .build();

        // when
        memberService.signup(memberSignup);

        // then
        assertThat(memberRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("memberSession에 맞는 member의 정보가 수정됩니다 - 성공")
    void update200() {
        // given
        Member member = saveMemberInRepository();

        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickname("수정 회원 닉네임")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        // when
        MemberResponse memberResponse = memberService.update(memberSession, memberUpdate);

        // then
        assertThat(memberResponse.getNickname()).isEqualTo(memberUpdate.getNickname());
        assertThat(memberResponse.getEmail()).isEqualTo(memberUpdate.getEmail());
    }

    @Test
    @DisplayName("memberSession에 맞는 member가 없으면 정보를 수정할 수 없습니다 - 실패")
    void update404() {
        // given
        MemberSession memberSession = MemberSession.builder()
                .id(1L)
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickname("수정 회원 닉네임")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        // expected
        assertThrows(MemberNotFoundException.class,
                () -> memberService.update(memberSession, memberUpdate));
    }

    @Test
    @DisplayName("MemberSession에 맞는 Member가 있다면 Member를 삭제합니다 - 성공")
    void delete200() {
        // given
        Member member = saveMemberInRepository();

        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();

        // when
        memberService.delete(memberSession);

        // then
        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("MemberSession에 맞는 Member가 없다면 Member 삭제를 할 수 없습니다 - 실패")
    void delete404() {
        // given
        MemberSession memberSession = MemberSession.builder()
                .id(1L)
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(MemberNotFoundException.class,
                () -> memberService.delete(memberSession));
    }

    @Test
    @DisplayName("회원이 존재하면 MemberSession을 생성합니다 - 성공")
    void makeMemberSession200() {
        // given
        Member member = saveMemberInRepository();

        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        MemberResponse memberResponse = memberService.login(memberLogin, request);

        // then
        assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname());
        assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
        HttpSession session = request.getSession(false);
        MemberSession findMemberSession = (MemberSession) session.getAttribute("memberSession");
        assertThat(findMemberSession.getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("회원이 존재하지 않으면 MemberSession을 생성하지 못합니다 - 성공")
    void makeMemberSession404() {
        // given
        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();

        // expected
        assertThrows(MemberNotFoundException.class,
                () -> memberService.login(memberLogin, request));
    }

    @Test
    @DisplayName("가입된 회원정보가 없으면 메소드를 통과합니다 - 성공")
    void checkDuplication200() {
        // given
        MemberSignup memberSignup = MemberSignup.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        memberService.checkDuplication(memberSignup);
    }

    @Test
    @DisplayName("이미 가입된 회원이라면 예외가 발생합니다 - 실패")
    void checkDuplication400() {
        // given
        saveMemberInRepository();

        MemberSignup memberSignup = MemberSignup.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(MemberDuplicationException.class,
                () -> memberService.checkDuplication(memberSignup));
    }

    @Test
    @DisplayName("이미 가입된 회원이라면 예외가 발생합니다 - 실패")
    void getPreviewContent() {
        // given
        saveMemberInRepository();

        MemberSignup memberSignup = MemberSignup.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(MemberDuplicationException.class,
                () -> memberService.checkDuplication(memberSignup));
    }

    @Test
    void logout() {
        // given
        MemberSession memberSession = MemberSession.builder()
                .id(1L)
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        memberService.logout(memberSession, httpServletRequest);

        // then
        HttpSession session = httpServletRequest.getSession(false);
        assertThat(session).isNull();
    }

    @Test
    @DisplayName("MemberSession에 맞는 Member가 있다면 Coin을 충전할 수 있습니다 - 성공")
    void chargeCoin200() {
        // given
        Member member = saveMemberInRepository();

        MemberSession memberSession = MemberSession.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();

        MemberCharge memberCharge = MemberCharge.builder()
                .chargeAmount(10000L)
                .build();

        // when
        memberService.chargeCoin(memberSession, memberCharge);
        Member findMember = memberRepository.getById(memberSession.getId());

        // then
        assertThat(findMember.getCoin()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("MemberSession에 맞는 Member가 없다면 Coin을 충전할 수 없습니다 - 실패")
    void chargeCoin404() {
        // given
        MemberSession memberSession = MemberSession.builder()
                .id(1L)
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MemberCharge memberCharge = MemberCharge.builder()
                .chargeAmount(10000L)
                .build();

        // expected
        assertThrows(MemberNotFoundException.class,
                () -> memberService.chargeCoin(memberSession, memberCharge));
    }
}