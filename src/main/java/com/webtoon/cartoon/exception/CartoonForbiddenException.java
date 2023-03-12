package com.webtoon.cartoon.exception;

import com.webtoon.global.error.ForbiddenException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class CartoonForbiddenException extends ForbiddenException {

    private static final String MESSAGE = CARTOON_FORBIDDEN.getValue();

    public CartoonForbiddenException() {
        super(MESSAGE);
    }
}
