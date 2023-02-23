package com.webtoon.cartoon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartoonListResult<T> {

    private long count;

    private T cartoonResponseList;
}
