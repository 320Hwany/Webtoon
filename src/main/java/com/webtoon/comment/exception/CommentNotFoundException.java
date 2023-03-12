package com.webtoon.comment.exception;

import com.webtoon.global.error.NotFoundException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class CommentNotFoundException extends NotFoundException {

    private static final String MESSAGE = COMMENT_NOT_FOUND.getValue();

    public CommentNotFoundException() {
        super(MESSAGE);
    }
}
