package com.webtoon.author.dto.request;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.response.AuthorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class AuthorSession implements Serializable {

    private Long id;

    private String nickName;

    private String email;
    private String password;

    @Builder
    public AuthorSession(Long id, String nickName, String email, String password) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public static AuthorSession getFromAuthor(Author author) {
        return AuthorSession.builder()
                .id(author.getId())
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();
    }
}
