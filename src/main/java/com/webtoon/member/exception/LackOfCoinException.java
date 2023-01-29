package com.webtoon.member.exception;

import lombok.Getter;

@Getter
public class LackOfCoinException extends RuntimeException {

    private final String statusCode = "400";
    private static final String MESSAGE = "코인이 부족합니다";

    public LackOfCoinException() {
        super(MESSAGE);
    }
}
