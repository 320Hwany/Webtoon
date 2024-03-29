package com.webtoon.member.domain;

import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;

import static com.webtoon.util.constant.ConstantCommon.MEMBER_SESSION;
import static org.assertj.core.api.Assertions.*;

class MemberSessionTest extends DomainTest {

    @Test
    void makeSession() {
        // given
        MemberSession memberSession = MemberSession.builder()
                .id(1L)
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        memberSession.makeSession(httpServletRequest);

        // then
        HttpSession session = httpServletRequest.getSession(false);
        MemberSession findMemberSession = (MemberSession) session.getAttribute(MEMBER_SESSION);
        assertThat(memberSession).isEqualTo(findMemberSession);
    }

    @Test
    void invalidate() {
        // given
        MemberSession memberSession = MemberSession.builder()
                .id(1L)
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        memberSession.invalidate(httpServletRequest);

        // then
        HttpSession session = httpServletRequest.getSession(false);
        assertThat(session).isNull();
    }
}