package com.webtoon.cartoon.exception;

import com.webtoon.global.error.ForbiddenException;

public class CartoonForbiddenException extends ForbiddenException {

    private static final String MESSAGE = "해당 만화에 접근 권한이 없습니다";

    public CartoonForbiddenException() {
        super(MESSAGE);
    }
}
