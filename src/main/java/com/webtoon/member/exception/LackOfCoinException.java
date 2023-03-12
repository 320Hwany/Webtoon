package com.webtoon.member.exception;

import lombok.Getter;

import static com.webtoon.util.constant.Constant.BAD_REQUEST;
import static com.webtoon.util.enumerated.ErrorMessage.*;

@Getter
public class LackOfCoinException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;
    private static final String MESSAGE = LACK_OF_COIN_BAD_REQUEST.getValue();

    public LackOfCoinException() {
        super(MESSAGE);
    }
}
