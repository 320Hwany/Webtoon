package com.webtoon.cartoonmember.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonMemberListResult<T> {

    private T cartoonMemberList;

    @Builder
    private CartoonMemberListResult(T cartoonMemberList) {
        this.cartoonMemberList = cartoonMemberList;
    }
}
