package com.webtoon.cartoon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartoonListResult<T> {

    private T cartoonResponseList;
}
