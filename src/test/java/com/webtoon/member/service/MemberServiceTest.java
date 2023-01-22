package com.webtoon.member.service;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.exception.MemberDuplicationException;
import com.webtoon.member.exception.MemberNotFoundException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class MemberServiceTest extends ServiceTest {

    @Autowired
    protected MemberService memberService;

    @Test
    @DisplayName("회원이 저장됩니다 - 성공")
    void signup() {
        // given
        MemberSignup memberSignup = MemberSignup.builder()
                .nickName("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // when
        memberService.signup(memberSignup);

        // then
        assertThat(memberRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("가입된 회원정보가 없으면 메소드를 통과합니다 - 성공")
    void checkDuplication200() {
        // given
        MemberSignup memberSignup = MemberSignup.builder()
                .nickName("회원 닉네임")
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
                .nickName("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(MemberDuplicationException.class,
                () -> memberService.checkDuplication(memberSignup));
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

        // when
        MemberSession memberSession = memberService.makeMemberSession(memberLogin);

        // then
        assertThat(memberSession.getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("회원이 존재하지 않으면 MemberSession을 생성하지 못합니다 - 성공")
    void makeMemberSession404() {
        // given
        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(MemberNotFoundException.class,
                () -> memberService.makeMemberSession(memberLogin));
    }

    @Test
    @DisplayName("MemberSession에 대한 세션을 생성합니다 - 성공")
    void makeSessionForMemberSession200() {
        // given
        Member member = saveMemberInRepository();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MemberSession memberSession = memberService.makeMemberSession(memberLogin);

        // when
        memberService.makeSessionForMemberSession(memberSession, httpServletRequest);

        // then
        HttpSession session = httpServletRequest.getSession(false);
        MemberSession findMemberSession = (MemberSession) session.getAttribute("memberSession");
        assertThat(findMemberSession.getId()).isEqualTo(member.getId());
    }
}