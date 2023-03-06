package com.webtoon.author.exception;

import com.webtoon.global.error.NotMatchException;

public class AuthorNotMatchException extends NotMatchException {

    private static final String MESSAGE = "이메일 또는 비밀번호가 일치하지 않습니다";

    public AuthorNotMatchException() {
        super(MESSAGE);
    }
}
