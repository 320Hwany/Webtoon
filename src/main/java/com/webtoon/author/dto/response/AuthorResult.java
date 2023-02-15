package com.webtoon.author.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorResult<T> {

    private T authorList;
}
