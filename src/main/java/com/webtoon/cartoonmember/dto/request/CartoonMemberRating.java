package com.webtoon.cartoonmember.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonMemberRating {

    private Long cartoonId;

    private Long memberId;

    private double rating;

    @Builder
    private CartoonMemberRating(Long cartoonId, Long memberId, double rating) {
        this.cartoonId = cartoonId;
        this.memberId = memberId;
        this.rating = rating;
    }

    public static CartoonMemberRating toCartoonMemberRating(Long cartoonId, Long memberId, double rating) {
        return CartoonMemberRating.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .rating(rating)
                .build();
    }
}
