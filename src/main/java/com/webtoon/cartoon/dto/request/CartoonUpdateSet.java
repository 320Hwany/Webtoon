package com.webtoon.cartoon.dto.request;

import com.webtoon.author.domain.AuthorSession;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonUpdateSet {

    private AuthorSession authorSession;

    private CartoonUpdate cartoonUpdate;

    private Long cartoonId;

    @Builder
    public CartoonUpdateSet(AuthorSession authorSession, CartoonUpdate cartoonUpdate, Long cartoonId) {
        this.authorSession = authorSession;
        this.cartoonUpdate = cartoonUpdate;
        this.cartoonId = cartoonId;
    }

    public static CartoonUpdateSet toCartoonUpdateSet(AuthorSession authorSession, CartoonUpdate cartoonUpdate,
                                               Long cartoonId) {
        return CartoonUpdateSet.builder()
                .authorSession(authorSession)
                .cartoonUpdate(cartoonUpdate)
                .cartoonId(cartoonId)
                .build();
    }
}
