package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorTest extends DomainTest {

    @Test
    @DisplayName("AuthorSignup 으로 부터 Author 를 생성합니다")
    void getFromAuthorSignup() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        // when
        Author author = Author.getFromAuthorSignup(authorSignup);

        // then
        assertThat(author.getNickName()).isEqualTo("작가 닉네임");
        assertThat(author.getEmail()).isEqualTo("yhwjd99@gmail.com");
        assertThat(author.getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("작가 정보를 수정합니다")
    void update() {
        // given
        Author author = getAuthor();

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