package com.webtoon.member.exception;

import com.webtoon.global.error.DuplicationException;

public class MemberDuplicationException extends DuplicationException {

    private static final String MESSAGE = "이미 가입된 회원이 있습니다";

    public MemberDuplicationException() {
        super(MESSAGE);
    }
}
