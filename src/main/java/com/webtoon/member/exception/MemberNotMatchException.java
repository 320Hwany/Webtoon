package com.webtoon.member.exception;

import com.webtoon.global.error.NotMatchException;

import static com.webtoon.util.enumerated.ErrorMessage.MEMBER_NOT_MATCH;

public class MemberNotMatchException extends NotMatchException {

    private static final String MESSAGE = MEMBER_NOT_MATCH.getValue();

    public MemberNotMatchException() {
        super(MESSAGE);
    }
}
