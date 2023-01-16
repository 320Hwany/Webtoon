package com.webtoon.cartoon.exception;

import com.webtoon.global.error.NotFoundException;

public class CartoonNotFoundException extends NotFoundException {

    private static final String MESSAGE = "만화를 찾을 수 없습니다";

    public CartoonNotFoundException() {
        super(MESSAGE);
    }
}
