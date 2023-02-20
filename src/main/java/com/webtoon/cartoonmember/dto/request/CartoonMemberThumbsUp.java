package com.webtoon.cartoonmember.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartoonMemberThumbsUp {

    private Long cartoonId;
    private Long memberId;

    @Builder
    public CartoonMemberThumbsUp(Long cartoonId, Long memberId) {
        this.cartoonId = cartoonId;
        this.memberId = memberId;
    }

    public static CartoonMemberThumbsUp getFromCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        return CartoonMemberThumbsUp.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .build();
    }
}
