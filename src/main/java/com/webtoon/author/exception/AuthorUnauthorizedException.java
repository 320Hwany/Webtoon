package com.webtoon.author.exception;

import com.webtoon.global.error.UnauthorizedException;

public class AuthorUnauthorizedException extends UnauthorizedException {

    private static final String MESSAGE = "작가님 로그인 후 이용해주세요";
    public AuthorUnauthorizedException() {
        super(MESSAGE);
    }
}
