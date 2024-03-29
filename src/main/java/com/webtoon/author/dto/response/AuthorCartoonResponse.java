package com.webtoon.author.dto.response;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorCartoonResponse {

    private String nickname;
    private long count;

    private List<CartoonResponse> cartoonResponseList;

    @Builder
    private AuthorCartoonResponse(String nickname, long count, List<CartoonResponse> cartoonResponseList) {
        this.nickname = nickname;
        this.count = count;
        this.cartoonResponseList = cartoonResponseList;
    }

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
