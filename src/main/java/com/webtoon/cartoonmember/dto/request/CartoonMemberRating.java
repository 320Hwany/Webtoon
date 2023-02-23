package com.webtoon.cartoonmember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonMemberRating {

    private Long cartoonId;
    private Long memberId;

    private double rating;

    public static CartoonMemberRating getFromIdAndRating(Long cartoonId, Long memberId, double rating) {
        return CartoonMemberRating.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .rating(rating)
                .build();
    }
}
