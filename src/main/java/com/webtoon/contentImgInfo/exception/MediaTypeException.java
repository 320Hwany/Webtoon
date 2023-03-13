package com.webtoon.contentImgInfo.exception;

import lombok.Getter;

import static com.webtoon.util.constant.ConstantCommon.BAD_REQUEST;
import static com.webtoon.util.enumerated.ErrorMessage.*;

@Getter
public class MediaTypeException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;
    private static final String MESSAGE = MEDIA_TYPE_BAD_REQUEST.getValue();

    public MediaTypeException() {
        super(MESSAGE);
    }
}
