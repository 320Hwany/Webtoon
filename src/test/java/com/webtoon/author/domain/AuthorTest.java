package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorTest {

    @Test
    @DisplayName("작가 정보 수정 성공")
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

}