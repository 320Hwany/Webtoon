package com.webtoon.global.error;

import lombok.Getter;

@Getter
public abstract class DuplicationException extends RuntimeException {

    private final String statusCode = "400";
    private String message;

    public DuplicationException(String message) {
        this.message = message;
    }
}
