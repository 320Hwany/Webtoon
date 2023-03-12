package com.webtoon.content.exception;

import com.webtoon.global.error.NotFoundException;
import lombok.Getter;

import static com.webtoon.util.enumerated.ErrorMessage.*;

@Getter
public class ContentNotFoundException extends NotFoundException {

    private static final String MESSAGE = CONTENT_NOT_FOUND.getValue();

    public ContentNotFoundException() {
        super(MESSAGE);
    }
}
