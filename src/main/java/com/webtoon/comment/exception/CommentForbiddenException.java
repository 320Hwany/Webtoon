package com.webtoon.comment.exception;

import com.webtoon.global.error.ForbiddenException;

public class CommentForbiddenException extends ForbiddenException {

    private static final String MESSAGE = "해당 댓글에 접근 권한이 없습니다";

    public CommentForbiddenException() {
        super(MESSAGE);
    }
}
