package com.webtoon.content.exception;

import com.webtoon.global.error.NotFoundException;
import lombok.Getter;

@Getter
public class ContentNotFoundException extends NotFoundException {

    private static final String MESSAGE = "컨텐츠를 찾을 수 없습니다";
    public ContentNotFoundException() {
        super(MESSAGE);
    }
}
