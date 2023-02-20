package com.webtoon.cartoonmember.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartoonMemberRating {

    private Long cartoonId;
    private Long memberId;

    private double rating;

    @Builder
    public CartoonMemberRating(Long cartoonId, Long memberId, double rating) {
        this.cartoonId = cartoonId;
        this.memberId = memberId;
        this.rating = rating;
    }

    public static CartoonMemberRating getFromIdAndRating(Long cartoonId, Long memberId, double rating) {
        return CartoonMemberRating.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .rating(rating)
                .build();
    }
}
