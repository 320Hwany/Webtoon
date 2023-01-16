package com.webtoon.cartoon.exception;

import lombok.Getter;

@Getter
public class EnumTypeValidException extends RuntimeException {

    private final String statusCode = "400";
    private static final String MESSAGE = "잘못된 요청입니다";

    public EnumTypeValidException() {
        super(MESSAGE);
    }
}
