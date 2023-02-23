package com.webtoon.cartoonmember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonMemberThumbsUp {

    private Long cartoonId;
    private Long memberId;

    public static CartoonMemberThumbsUp getFromCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        return CartoonMemberThumbsUp.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .build();
    }
}
