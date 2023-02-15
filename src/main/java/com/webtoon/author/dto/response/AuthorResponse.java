package com.webtoon.author.dto.response;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AuthorResponse {

    private String nickName;
    private String email;

    @Builder
    public AuthorResponse(String nickName, String email) {
        this.nickName = nickName;
        this.email = email;
    }

    public static AuthorResponse getFromAuthorSession(AuthorSession authorSession) {
        return AuthorResponse.builder()
                .nickName(authorSession.getNickName())
                .email(authorSession.getEmail())
                .build();
    }

    public static AuthorResponse getFromAuthor(Author author) {
        return AuthorResponse.builder()
                .nickName(author.getNickName())
                .email(author.getEmail())
                .build();
    }

    public static List<AuthorResponse> getFromAuthorList(List<Author> authorList) {
        return authorList.stream()
                .map(AuthorResponse::getFromAuthor)
                .collect(Collectors.toList());
    }
}
