package com.webtoon.cartoonmember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartoonMemberListResult<T> {

    private T cartoonMemberList;
}
