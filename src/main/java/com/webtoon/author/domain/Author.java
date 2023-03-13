package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Author extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "author_id")
    private Long id;

    private String nickname;

    private String email;

    private String password;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Cartoon> cartoonList = new ArrayList<>();

    @Builder
    public Author(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public void update(AuthorUpdate authorUpdate, PasswordEncoder passwordEncoder) {
        this.nickname = authorUpdate.getNickname();
        this.email = authorUpdate.getEmail();
        this.password = passwordEncoder.encode(authorUpdate.getPassword());
    }
}
