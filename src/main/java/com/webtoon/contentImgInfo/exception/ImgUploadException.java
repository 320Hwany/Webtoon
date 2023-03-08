package com.webtoon.contentImgInfo.exception;

import lombok.Getter;

@Getter
public class ImgUploadException extends RuntimeException {

    private final String statusCode = "400";
    private static final String MESSAGE = "해당 이미지를 업로드 할 수 없습니다";

    public ImgUploadException() {
        super(MESSAGE);
    }
}
