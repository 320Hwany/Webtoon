package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorUpdate;
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

    public void update(AuthorUpdate authorUpdate) {
        this.nickName = authorUpdate.getNickName();
        this.email = authorUpdate.getEmail();
        this.password = authorUpdate.getPassword();
    }
}
