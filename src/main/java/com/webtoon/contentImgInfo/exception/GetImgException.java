package com.webtoon.contentImgInfo.exception;

import lombok.Getter;

@Getter
public class GetImgException extends RuntimeException {

    private final String statusCode = "400";
    private static final String MESSAGE = "해당 이미지를 가져올 수 없습니다";

    public GetImgException() {
        super(MESSAGE);
    }
}
