package com.webtoon.author.exception;

import com.webtoon.global.error.NotFoundException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class AuthorNotFoundException extends NotFoundException {

    private static final String MESSAGE = AUTHOR_NOT_FOUND.getValue();

    public AuthorNotFoundException() {
        super(MESSAGE);
    }
}
