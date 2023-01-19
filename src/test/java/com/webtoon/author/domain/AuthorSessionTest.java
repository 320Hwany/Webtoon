package com.webtoon.author.domain;

import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorSessionTest extends DomainTest {

    @Test
    @DisplayName("Author 으로 부터 AuthorSession 를 생성합니다")
    void getFromAuthor() {
        // given
        Author author = getAuthor();

        // when
        AuthorSession authorSession = AuthorSession.getFromAuthor(author);

        // then
        assertThat(authorSession.getNickName()).isEqualTo("작가 닉네임");
        assertThat(authorSession.getEmail()).isEqualTo("yhwjd99@gmail.com");
        assertThat(authorSession.getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("세션을 생성합니다")
    void makeSession() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .nickName("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        authorSession.makeSession(httpServletRequest);

        // then
        HttpSession findSession = httpServletRequest.getSession(false);
        AuthorSession findAuthorSession = (AuthorSession) findSession.getAttribute("authorSession");
        assertThat(findSession).isNotNull();
        assertThat(findAuthorSession).isEqualTo(authorSession);
    }

    @Test
    @DisplayName("작가 로그인 세션을 삭제합니다")
    void invalidateSession() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .nickName("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("SessionTest", authorSession);

        // when
        authorSession.invalidateSession(httpServletRequest);

        // then
        HttpSession findSession = httpServletRequest.getSession(false);
        assertThat(findSession).isNull();
    }
}
