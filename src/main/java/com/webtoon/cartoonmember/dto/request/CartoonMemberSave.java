package com.webtoon.cartoonmember.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartoonMemberSave {

    private Long cartoonId;
    private Long memberId;

    @Builder
    public CartoonMemberSave(Long cartoonId, Long memberId) {
        this.cartoonId = cartoonId;
        this.memberId = memberId;
    }

    public static CartoonMemberSave getFromCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        return CartoonMemberSave.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .build();
    }
}
