package com.webtoon.author.exception;

import com.webtoon.global.error.NotMatchException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class AuthorNotMatchException extends NotMatchException {

    private static final String MESSAGE = AUTHOR_NOT_MATCH.getValue();

    public AuthorNotMatchException() {
        super(MESSAGE);
    }
}
