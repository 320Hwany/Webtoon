package com.webtoon.author.domain;

import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_LONG;
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
