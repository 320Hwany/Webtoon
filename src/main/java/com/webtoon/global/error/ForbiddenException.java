package com.webtoon.global.error;

import lombok.Getter;

import static com.webtoon.util.constant.ConstantCommon.*;

@Getter
public abstract class ForbiddenException extends RuntimeException {

    private final String statusCode = FORBIDDEN;
    private String message;

    public ForbiddenException(String message) {
        this.message = message;
    }
}
