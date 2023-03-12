package com.webtoon.author.exception;

import com.webtoon.global.error.DuplicationException;

import static com.webtoon.util.enumerated.ErrorMessage.AUTHOR_DUPLICATION;

public class AuthorDuplicationException extends DuplicationException {

    private static final String MESSAGE = AUTHOR_DUPLICATION.getValue();

    public AuthorDuplicationException() {
        super(MESSAGE);
    }
}
