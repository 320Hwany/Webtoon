package com.webtoon.author.exception;

import com.webtoon.global.error.NotFoundException;

public class AuthorNotFoundException extends NotFoundException {

    private static final String MESSAGE = "작가를 찾을 수 없습니다";

    public AuthorNotFoundException() {
        super(MESSAGE);
    }
}
