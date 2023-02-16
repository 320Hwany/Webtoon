package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorTest extends DomainTest {


    @Test
    @DisplayName("AuthorSignup 으로 부터 Author 를 생성합니다")
    void getFromAuthorSignup() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickname("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        // when
        Author author = Author.getFromAuthorSignup(authorSignup, passwordEncoder);

        // then
        assertThat(author.getNickname()).isEqualTo("작가 닉네임");
        assertThat(author.getEmail()).isEqualTo("yhwjd99@gmail.com");
        assertThat(passwordEncoder.matches("1234", author.getPassword())).isTrue();
    }

    @Test
    @DisplayName("작가 정보를 수정합니다")
    void update() {
        // given
        Author author = getAuthor();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickname("수정 닉네임")
                .email("수정 이메일")
                .password("4321")
                .build();

        // when
        author.update(authorUpdate, passwordEncoder);

        // then
        assertThat(author.getId()).isEqualTo(author.getId());
        assertThat(author.getNickname()).isEqualTo("수정 닉네임");
        assertThat(author.getEmail()).isEqualTo("수정 이메일");
        assertThat(passwordEncoder.matches("4321", author.getPassword())).isTrue();
    }
}