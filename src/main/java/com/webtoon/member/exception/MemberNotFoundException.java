package com.webtoon.member.exception;

import com.webtoon.global.error.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    private static final String MESSAGE = "회원을 찾을 수 없습니다";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
