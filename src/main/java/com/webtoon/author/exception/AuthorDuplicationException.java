package com.webtoon.author.exception;

import com.webtoon.global.error.DuplicationException;

public class AuthorDuplicationException extends DuplicationException {

    private static final String MESSAGE = "이미 가입된 작가 회원이 있습니다";

    public AuthorDuplicationException() {
        super(MESSAGE);
    }
}
