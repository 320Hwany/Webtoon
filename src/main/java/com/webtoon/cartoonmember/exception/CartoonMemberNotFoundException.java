package com.webtoon.cartoonmember.exception;

import com.webtoon.global.error.NotFoundException;

import static com.webtoon.util.enumerated.ErrorMessage.CARTOON_MEMBER_NOT_FOUND;

public class CartoonMemberNotFoundException extends NotFoundException {

    private static final String MESSAGE = CARTOON_MEMBER_NOT_FOUND.getValue();

    public CartoonMemberNotFoundException() {
        super(MESSAGE);
    }
}
