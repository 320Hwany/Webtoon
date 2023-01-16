package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "author_id")
    private Long id;

    private String nickName;

    private String email;
    private String password;

    @Builder
    public Author(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public AuthorResponse getAuthorResponse() {
        return AuthorResponse.builder()
                .nickName(nickName)
                .email(email)
                .password(password)
                .build();
    }

    public AuthorSession getAuthorSession() {
        return AuthorSession.builder()
                .id(id)
                .nickName(nickName)
                .email(email)
                .password(password)
                .build();
    }

    public void update(AuthorUpdate authorUpdate) {
        this.nickName = authorUpdate.getNickName();
        this.email = authorUpdate.getEmail();
        this.password = authorUpdate.getPassword();
    }
}
