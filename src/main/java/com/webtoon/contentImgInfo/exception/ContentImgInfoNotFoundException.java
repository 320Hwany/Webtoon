package com.webtoon.contentImgInfo.exception;

import com.webtoon.global.error.NotFoundException;

import static com.webtoon.util.enumerated.ErrorMessage.*;

public class ContentImgInfoNotFoundException extends NotFoundException {

    private static final String MESSAGE = CONTENT_IMG_INFO_NOT_FOUND.getValue();

    public ContentImgInfoNotFoundException() {
        super(MESSAGE);
    }
}
