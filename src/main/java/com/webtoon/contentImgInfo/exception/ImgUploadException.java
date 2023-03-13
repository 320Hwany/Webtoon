package com.webtoon.contentImgInfo.exception;

import lombok.Getter;

import static com.webtoon.util.constant.ConstantCommon.BAD_REQUEST;
import static com.webtoon.util.enumerated.ErrorMessage.*;

@Getter
public class ImgUploadException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;
    private static final String MESSAGE = IMG_UPLOAD_BAD_REQUEST.getValue();

    public ImgUploadException() {
        super(MESSAGE);
    }
}
