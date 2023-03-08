package com.webtoon.contentImgInfo.exception;

import lombok.Getter;

@Getter
public class MediaTypeException extends RuntimeException {

    private final String statusCode = "400";
    private static final String MESSAGE = "MediaType을 알 수 없습니다";

    public MediaTypeException() {
        super(MESSAGE);
    }
}
