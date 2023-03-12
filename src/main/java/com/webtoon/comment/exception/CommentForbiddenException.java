package com.webtoon.comment.exception;

import com.webtoon.global.error.ForbiddenException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class CommentForbiddenException extends ForbiddenException {

    private static final String MESSAGE = COMMENT_FORBIDDEN.getValue();

    public CommentForbiddenException() {
        super(MESSAGE);
    }
}
