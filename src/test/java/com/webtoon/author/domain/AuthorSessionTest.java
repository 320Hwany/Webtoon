package com.webtoon.author.domain;

import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;

import static com.webtoon.util.constant.ConstantCommon.AUTHOR_SESSION;
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
        assertThat(authorSession.getNickname()).isEqualTo("작가 닉네임");
        assertThat(authorSession.getEmail()).isEqualTo("yhwjd99@gmail.com");
        assertThat(authorSession.getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("세션을 생성합니다")
    void makeSession() {
        // given
        AuthorSession authorSession = getAuthorSession();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        authorSession.makeSession(httpServletRequest);

        // then
        HttpSession findSession = httpServletRequest.getSession(false);
        AuthorSession findAuthorSession = (AuthorSession) findSession.getAttribute(AUTHOR_SESSION);
        assertThat(findSession).isNotNull();
        assertThat(findAuthorSession).isEqualTo(authorSession);
    }

    @Test
    @DisplayName("작가 로그인 세션을 삭제합니다")
    void invalidateSession() {
        // given
        AuthorSession authorSession = getAuthorSession();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(AUTHOR_SESSION, authorSession);

        // when
        authorSession.invalidateSession(httpServletRequest);

        // then
        HttpSession findSession = httpServletRequest.getSession(false);
        assertThat(findSession).isNull();
    }
}
