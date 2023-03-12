package com.webtoon.author.exception;

import com.webtoon.global.error.UnauthorizedException;

import static com.webtoon.util.enumerated.ErrorMessage.AUTHOR_UNAUTHORIZED;

public class AuthorUnauthorizedException extends UnauthorizedException {

    private static final String MESSAGE = AUTHOR_UNAUTHORIZED.getValue();
    public AuthorUnauthorizedException() {
        super(MESSAGE);
    }
}
