package com.webtoon.author.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Objects;

import static com.webtoon.util.constant.ConstantCommon.AUTHOR_SESSION;

@Getter
@NoArgsConstructor
public class AuthorSession implements Serializable {

    private Long id;

    private String nickname;

    private String email;

    private String password;

    @Builder
    public AuthorSession(Long id, String nickname, String email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static AuthorSession getFromAuthor(Author author) {
        return AuthorSession.builder()
                .id(author.getId())
                .nickname(author.getNickname())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();
    }

    public void makeSession(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(AUTHOR_SESSION, this);
    }

    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
