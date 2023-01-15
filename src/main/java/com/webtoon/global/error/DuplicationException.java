package com.webtoon.global.error;

public abstract class DuplicationException extends RuntimeException {

    private final String statusCode = "400";
    private String message;

    public DuplicationException(String message) {
        this.message = message;
    }
}
