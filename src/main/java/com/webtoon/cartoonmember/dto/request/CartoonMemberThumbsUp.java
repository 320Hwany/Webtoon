package com.webtoon.cartoonmember.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonMemberThumbsUp {

    private Long cartoonId;
    private Long memberId;

    @Builder
    private CartoonMemberThumbsUp(Long cartoonId, Long memberId) {
        this.cartoonId = cartoonId;
        this.memberId = memberId;
    }

    public static CartoonMemberThumbsUp toCartoonMemberThumbsUp(Long cartoonId, Long memberId) {
        return CartoonMemberThumbsUp.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .build();
    }
}
