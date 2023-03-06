package com.webtoon.author.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceTest extends ServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("중복 검증, 회원 가입 트랜잭션 세트를 진행합니다")
    void signupSet() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickname("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        // when
        authorService.signupSet(authorSignup);

        // then
        assertThat(authorRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("중복 검증, 회원 가입 트랜잭션 세트를 진행합니다")
    void loginSet() {
        // given
        Author author = saveAuthorInRepository();

        AuthorLogin authorLogin = AuthorLogin.builder()
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        AuthorResponse authorResponse = authorService.loginSet(authorLogin, httpServletRequest);

        // then
        assertThat(authorResponse.getNickname()).isEqualTo(author.getNickname());
        assertThat(authorResponse.getEmail()).isEqualTo("yhwjd99@gmail.com");
    }

    @Test
    @DisplayName("AuthorId로 작가 회원을 찾고 들어온 정보로 수정합니다")
    void updateSet200() {
        // given
        Author author = saveAuthorInRepository();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickname("수정 닉네임")
                .email("수정 이메일")
                .password("4321")
                .build();

        // when
        AuthorResponse authorResponse = authorService.updateSet(author.getId(), authorUpdate);

        // then
        assertThat(authorResponse.getNickname()).isEqualTo("수정 닉네임");
        assertThat(authorResponse.getEmail()).isEqualTo("수정 이메일");
    }

    @Test
    @DisplayName("AuthorId와 일치하는 작가 회원이 없다면 예외가 발생합니다")
    void updateSet404() {
        // given
        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickname("수정 닉네임")
                .email("수정 이메일")
                .password("4321")
                .build();

        // expected
        Assertions.assertThrows(AuthorNotFoundException.class,
                () -> authorService.updateSet(9999L, authorUpdate));
    }

    @Test
    @DisplayName("AuthorId로 작가 회원을 찾고 작가를 삭제합니다")
    void deleteSet200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        author.getCartoonList().add(cartoon);
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        authorService.deleteSet(authorSession, httpServletRequest);

        // then
        assertThat(authorRepository.count()).isEqualTo(0);
        assertThat(cartoonRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("AuthorId와 일치하는 작가가 없다면 예외가 발생합니다")
    void deleteSet404() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .id(9999L)
                .build();

        // expected
        Assertions.assertThrows(AuthorNotFoundException.class,
                () -> authorService.deleteSet(authorSession, new MockHttpServletRequest()));
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 메소드를 에러없이 메소드를 통과합니다")
    void checkDuplication200() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickname("새로운 회원")
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
                .nickname(author.getNickname())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        // expected
        assertThrows(AuthorDuplicationException.class,
                () -> authorService.checkDuplication(authorSignup));
    }

    @Test
    @DisplayName("회원가입이 성공합니다")
    void signup() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickname("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        // when
        authorService.signup(authorSignup);

        // then
        assertThat(authorRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원정보가 존재하면 AuthorSession 을 생성합니다")
    void makeAuthorSession200() {
        // given
        Author author = saveAuthorInRepository();

        AuthorLogin authorLogin = AuthorLogin.builder()
                .email(author.getEmail())
                .password("1234")
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
    void makeSessionForAuthorSession() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .nickname("작가 이름")
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
    void invalidateSession() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .nickname("작가 이름")
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

    @Test
    @DisplayName("입력한 닉네임이 포함된 작가를 검색하여 입력한 정보에 맞는 페이지를 반환합니다 - 성공")
    void findAllByNicknameContains() {
        // given
        Author author = saveAuthorInRepository();
        saveCartoonInRepository(author);

        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .nickname("작가 이름")
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("NONE")
                .build();

        // when
        List<AuthorCartoonResponse> authorCartoonResponseList =
                authorService.findAllByNicknameContains(cartoonSearchDto);

        // then
        AuthorCartoonResponse authorCartoonResponse = authorCartoonResponseList.get(0);
        List<CartoonResponse> cartoonResponseList = authorCartoonResponse.getCartoonResponseList();

        assertThat(authorCartoonResponseList.size()).isEqualTo(1);
        assertThat(cartoonResponseList.size()).isEqualTo(1);
    }
}