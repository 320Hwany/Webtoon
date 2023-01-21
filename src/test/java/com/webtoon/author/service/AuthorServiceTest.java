package com.webtoon.author.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class AuthorServiceTest extends ServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("회원가입이 성공합니다")
    void signup200() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        // when
        authorService.signup(authorSignup);

        // then
        assertThat(authorRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("입력한 닉네임이 포함된 작가를 검색합니다 - 성공")
    void findAllByNickName200() {
        // given
        Author author = saveAuthorInRepository();

        // when
        List<Author> authorList = authorService.findAllByNickName(author.getNickName());

        // then
        assertThat(authorList.get(0)).isEqualTo(author);
    }

    @Test
    @DisplayName("작가 정보가 수정됩니다 - 성공")
    void update200() {
        // given
        Author author = saveAuthorInRepository();

        AuthorSession authorSession = AuthorSession.builder()
                .id(author.getId())
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickName("수정 닉네임")
                .email("수정 이메일")
                .password("4321")
                .build();

        // when
        Author afterUpdate = authorService.update(authorSession, authorUpdate);

        // then
        assertThat(afterUpdate.getNickName()).isEqualTo("수정 닉네임");
        assertThat(afterUpdate.getEmail()).isEqualTo("수정 이메일");
        assertThat(afterUpdate.getPassword()).isEqualTo("4321");
    }

    @Test
    @DisplayName("존재하지 않는 작가 계정이면 수정이 되지 않습니다 - 실패")
    void update404() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .id(1L)
                .nickName("DB에 없는 회원")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickName("수정 닉네임")
                .email("수정 이메일")
                .password("4321")
                .build();

        // expected
        assertThrows(AuthorNotFoundException.class,
                () -> authorService.update(authorSession, authorUpdate));
    }

    @Test
    @DisplayName("작가 계정이 삭제됩니다 - 성공")
    void delete200() {
        // given
        Author author = saveAuthorInRepository();

        AuthorSession authorSession = AuthorSession.builder()
                .id(author.getId())
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        // when
        authorService.delete(authorSession);

        // then
        Optional<Author> optionalAuthor = authorRepository.findByNickName(author.getNickName());
        assertThat(optionalAuthor).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 작가 계정이면 삭제할 수 없습니다 - 실패")
    void delete404() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .id(1L)
                .nickName("DB에 없는 회원")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(AuthorNotFoundException.class,
                () -> authorService.delete(authorSession));
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 메소드를 에러없이 메소드를 통과합니다")
    void checkDuplication200() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName("새로운 회원")
                .email("yhwjd99@gmail.com")
                .password("4321")
                .build();

        // expected
        authorService.checkDuplication(authorSignup);
    }

    @Test
    @DisplayName("이미 존재하는 회원이면 에러 메세지를 보여줍니다")
    void checkDuplication404() {
        // given
        Author author = saveAuthorInRepository();

        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        // expected
        assertThrows(AuthorDuplicationException.class,
                () -> authorService.checkDuplication(authorSignup));
    }

    @Test
    @DisplayName("회원정보가 존재하면 AuthorSession 을 생성합니다")
    void makeAuthorSession200() {
        // given
        Author author = saveAuthorInRepository();

        AuthorLogin authorLogin = AuthorLogin.builder()
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        // when
        AuthorSession authorSession = authorService.makeAuthorSession(authorLogin);

        // then
        assertThat(authorSession).isNotNull();
    }

    @Test
    @DisplayName("회원정보가 존재하지 않으면 AuthorSession 을 생성할 수 없습니다 - 실패")
    void makeAuthorSession404() {
        // given
        AuthorLogin authorLogin = AuthorLogin.builder()
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        // expected
        assertThrows(AuthorNotFoundException.class,
                () -> authorService.makeAuthorSession(authorLogin));
    }

    @Test
    @DisplayName("AuthorSession 의 세션을 생성합니다")
    void makeSessionForAuthorSession200() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .nickName("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        authorService.makeSessionForAuthorSession(authorSession, httpServletRequest);

        // then
        HttpSession session = httpServletRequest.getSession(false);
        assertThat(session).isNotNull();
    }

    @Test
    @DisplayName("AuthorSession 의 세션을 삭제합니다")
    void invalidateSession200() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .nickName("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        authorSession.makeSession(httpServletRequest);
        // when
        authorService.invalidateSession(authorSession, httpServletRequest);

        // then
        HttpSession session = httpServletRequest.getSession(false);
        assertThat(session).isNull();
    }
}