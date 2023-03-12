package com.webtoon.contentImgInfo.exception;

import lombok.Getter;

import static com.webtoon.util.constant.Constant.*;
import static com.webtoon.util.enumerated.ErrorMessage.*;

@Getter
public class GetImgException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;
    private static final String MESSAGE = GET_IMG_BAD_REQUEST.getValue();

    public GetImgException() {
        super(MESSAGE);
    }
}
