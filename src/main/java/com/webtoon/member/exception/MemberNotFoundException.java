package com.webtoon.member.exception;

import com.webtoon.global.error.NotFoundException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class MemberNotFoundException extends NotFoundException {

    private static final String MESSAGE = MEMBER_NOT_FOUND.getValue();

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
