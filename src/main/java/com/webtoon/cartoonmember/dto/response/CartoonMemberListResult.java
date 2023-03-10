package com.webtoon.cartoonmember.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartoonMemberListResult<T> {

    private T cartoonMemberList;
}
