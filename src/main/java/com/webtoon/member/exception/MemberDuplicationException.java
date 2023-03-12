package com.webtoon.member.exception;

import com.webtoon.global.error.DuplicationException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class MemberDuplicationException extends DuplicationException {

    private static final String MESSAGE = MEMBER_DUPLICATION.getValue();

    public MemberDuplicationException() {
        super(MESSAGE);
    }
}
