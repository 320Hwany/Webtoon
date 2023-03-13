package com.webtoon.cartoon.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.webtoon.util.constant.ConstantCommon.*;
import static com.webtoon.util.enumerated.ErrorMessage.*;

@Getter
public class EnumTypeValidException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;
    private static final String MESSAGE = ENUM_TYPE_VALIDATION.getValue();

    private Map<String, String> validation = new HashMap<>();

    public EnumTypeValidException(Boolean isDayValid, Boolean isProgressValid, Boolean isGenreValid) {
        super(MESSAGE);
        if (!isDayValid) {
            validation.put(DAY_OF_THE_WEEk, DAY_OF_THE_WEEK_BAD_REQUEST.getValue());
        }
        if (!isProgressValid) {
            validation.put(PROGRESS, PROGRESS_BAD_REQUEST.getValue());
        }
        if (!isGenreValid) {
            validation.put(GENRE, GENRE_BAD_REQUEST.getValue());
        }
    }
}
