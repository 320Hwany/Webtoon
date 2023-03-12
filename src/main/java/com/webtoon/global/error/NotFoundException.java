package com.webtoon.global.error;

import lombok.Getter;

import static com.webtoon.util.constant.Constant.*;

@Getter
public abstract class NotFoundException extends RuntimeException {

    private final String statusCode = NOT_FOUND;
    private String message;

    public NotFoundException(String message) {
        this.message = message;
    }
}
