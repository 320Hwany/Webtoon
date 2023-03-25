package com.webtoon.cartoonmember.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonMemberSave {

    private Long cartoonId;
    private Long memberId;

    @Builder
    public CartoonMemberSave(Long cartoonId, Long memberId) {
        this.cartoonId = cartoonId;
        this.memberId = memberId;
    }

    public static CartoonMemberSave toCartoonMemberSave(Long cartoonId, Long memberId) {
        return CartoonMemberSave.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .build();
    }
}
