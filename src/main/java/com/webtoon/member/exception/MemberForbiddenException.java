package com.webtoon.member.exception;

import com.webtoon.global.error.ForbiddenException;

import static com.webtoon.util.enumerated.ErrorMessage.MEMBER_FORBIDDEN;

public class MemberForbiddenException extends ForbiddenException {

    private static final String MESSAGE = MEMBER_FORBIDDEN.getValue();

    public MemberForbiddenException() {
        super(MESSAGE);
    }
}
