package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Author extends BaseTimeEntity {

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

    public static Author getFromAuthorSignup(AuthorSignup authorSignup) {
        return Author.builder()
                .nickName(authorSignup.getNickName())
                .email(authorSignup.getEmail())
                .password(authorSignup.getPassword())
                .build();
    }

    public void update(AuthorUpdate authorUpdate) {
        this.nickName = authorUpdate.getNickName();
        this.email = authorUpdate.getEmail();
        this.password = authorUpdate.getPassword();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(nickName, author.nickName)
                && Objects.equals(email, author.email) && Objects.equals(password, author.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickName, email, password);
    }
}
