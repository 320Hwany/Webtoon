package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.dto.request.AuthorUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorTest {

    @Test
    @DisplayName("작가 정보를 수정합니다")
    void update() {
        // given
        Author author = Author.builder()
                .nickName("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickName("수정 닉네임")
                .email("수정 이메일")
                .password("4321")
                .build();

        // when
        author.update(authorUpdate);

        // then
        assertThat(author.getId()).isEqualTo(author.getId());
        assertThat(author.getNickName()).isEqualTo("수정 닉네임");
        assertThat(author.getEmail()).isEqualTo("수정 이메일");
        assertThat(author.getPassword()).isEqualTo("4321");
    }

    @Test
    @DisplayName("작가 로그인 세션을 삭제합니다")
    void invalidateSession() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .nickName("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("SessionTest", authorSession);

        // when
        Author.invalidateSession(httpServletRequest);

        // then
        HttpSession findSession = httpServletRequest.getSession(false);
        assertThat(findSession).isNull();
    }
}