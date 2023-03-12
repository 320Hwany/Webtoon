package com.webtoon.global.error;

import lombok.Getter;

import static com.webtoon.util.constant.Constant.*;

@Getter
public abstract class UnauthorizedException extends RuntimeException {

    private final String statusCode = UNAUTHORIZED;
    private String message;

    public UnauthorizedException(String message) {
        this.message = message;
    }
}
