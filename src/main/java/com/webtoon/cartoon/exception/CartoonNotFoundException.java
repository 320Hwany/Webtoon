package com.webtoon.cartoon.exception;

import com.webtoon.global.error.NotFoundException;

import static com.webtoon.util.enumerated.ErrorMessage.CARTOON_NOT_FOUND;

public class CartoonNotFoundException extends NotFoundException {

    private static final String MESSAGE = CARTOON_NOT_FOUND.getValue();

    public CartoonNotFoundException() {
        super(MESSAGE);
    }
}
