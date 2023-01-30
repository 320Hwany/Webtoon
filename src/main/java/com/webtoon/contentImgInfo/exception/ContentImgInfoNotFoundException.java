package com.webtoon.contentImgInfo.exception;

import com.webtoon.global.error.NotFoundException;

public class ContentImgInfoNotFoundException extends NotFoundException {

    private static final String MESSAGE ="컨텐츠 이미지를 찾을 수 없습니다";

    public ContentImgInfoNotFoundException() {
        super(MESSAGE);
    }
}
