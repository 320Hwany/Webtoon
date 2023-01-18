package com.webtoon.author.dto.response;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorResponse {

    private String nickName;
    private String email;
    private String password;

    @Builder
    public AuthorResponse(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public static AuthorResponse getFromAuthorSession(AuthorSession authorSession) {
        return AuthorResponse.builder()
                .nickName(authorSession.getNickName())
                .email(authorSession.getEmail())
                .password(authorSession.getPassword())
                .build();
    }

    public static AuthorResponse getFromAuthor(Author author) {
        return AuthorResponse.builder()
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();
    }
}
