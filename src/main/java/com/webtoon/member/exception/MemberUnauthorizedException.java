package com.webtoon.member.exception;

import com.webtoon.global.error.UnauthorizedException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class MemberUnauthorizedException extends UnauthorizedException {

    private static final String MESSAGE = MEMBER_UNAUTHORIZED.getValue();

    public MemberUnauthorizedException() {
        super(MESSAGE);
    }
}
