package com.webtoon.cartoonmember.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonMemberSave {

    private Long cartoonId;
    private Long memberId;

    public static CartoonMemberSave getFromCartoonIdAndMemberId(Long cartoonId, Long memberId) {
        return CartoonMemberSave.builder()
                .cartoonId(cartoonId)
                .memberId(memberId)
                .build();
    }
}
