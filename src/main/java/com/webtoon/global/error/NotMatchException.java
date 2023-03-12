package com.webtoon.global.error;

import lombok.Getter;

import static com.webtoon.util.constant.Constant.BAD_REQUEST;

@Getter
public abstract class NotMatchException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;

    private String message;

    public NotMatchException(String message) {
        this.message = message;
    }
}
