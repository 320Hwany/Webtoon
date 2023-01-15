package com.webtoon.author.dto.request;

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
}
