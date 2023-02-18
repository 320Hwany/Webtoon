package com.webtoon.author.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorListResult<T> {

    private long count;

    private T authorResponseList;
}
