package com.webtoon.global.error;

import lombok.Getter;

@Getter
public abstract class ForbiddenException extends RuntimeException {

    private final String statusCode = "403";
    private String message;
    public ForbiddenException(String message) {
        this.message = message;
    }
}
