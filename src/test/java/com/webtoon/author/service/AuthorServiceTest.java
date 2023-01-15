package com.webtoon.author.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceTest extends ServiceTest {

    @Autowired
    protected AuthorService authorService;

    @BeforeEach
    void clean() {
        authorRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입이 성공합니다")
    void signupSuccess() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        // when
        authorService.signup(authorSignup);

        // then
        assertThat(authorRepository.findByNickName("작가 닉네임")).isPresent();
        assertThat(authorRepository.findByEmail("yhwjd99@gmail.com")).isPresent();
    }

    @Test
    @DisplayName("중복된 닉네임이나 이메일로 회원가입을 할 수 없습니다 - 실패")
    void signupFail() {
        // given
        Author author = saveAuthorInRepository();

        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        // expected
        assertThrows(AuthorDuplicationException.class,
                () -> authorService.signup(authorSignup));
    }

    @Test
    @DisplayName("회원정보가 존재하면 AuthorSession 을 생성합니다")
    void makeAuthorSession() {
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
    void makeAuthorSessionFail() {
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
    @DisplayName("이미 존재하는 회원이면 에러 메세지를 보여줍니다")
    void checkDuplication() {
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
    @DisplayName("작가 정보 수정에 성공합니다")
    void updateSuccess() {
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
    @DisplayName("작가 계정 삭제 - 성공")
    void delete() {
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
}