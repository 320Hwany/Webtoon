package com.webtoon.global.error;

import lombok.Getter;

import static com.webtoon.util.constant.Constant.*;

@Getter
public abstract class DuplicationException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;
    private String message;

    public DuplicationException(String message) {
        this.message = message;
    }
}
