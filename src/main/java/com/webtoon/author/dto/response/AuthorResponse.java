package com.webtoon.author.dto.response;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AuthorResponse {

    private String nickname;
    private String email;

    @Builder
    public AuthorResponse(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public static AuthorResponse getFromAuthorSession(AuthorSession authorSession) {
        return AuthorResponse.builder()
                .nickname(authorSession.getNickname())
                .email(authorSession.getEmail())
                .build();
    }

    public static AuthorResponse getFromAuthor(Author author) {
        return AuthorResponse.builder()
                .nickname(author.getNickname())
                .email(author.getEmail())
                .build();
    }

    public static List<AuthorResponse> getFromAuthorList(List<Author> authorList) {
        return authorList.stream()
                .map(AuthorResponse::getFromAuthor)
                .collect(Collectors.toList());
    }
}
