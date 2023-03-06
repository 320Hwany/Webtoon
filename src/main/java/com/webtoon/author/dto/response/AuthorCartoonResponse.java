package com.webtoon.author.dto.response;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCartoonResponse {

    private String nickname;
    private long count;

    private List<CartoonResponse> cartoonResponseList;

    public static AuthorCartoonResponse getFromAuthor(Author author) {
        return AuthorCartoonResponse.builder()
                .nickname(author.getNickname())
                .count(author.getCartoonList().size())
                .cartoonResponseList(
                        author.getCartoonList().stream()
                        .map(CartoonResponse::getFromCartoon)
                        .collect(Collectors.toList()))
                .build();
    }
}
