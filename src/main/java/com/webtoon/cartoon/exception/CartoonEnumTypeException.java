package com.webtoon.cartoon.exception;

import com.webtoon.global.error.EnumTypeException;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.webtoon.util.constant.ConstantCommon.*;
import static com.webtoon.util.enumerated.ErrorMessage.*;

@Getter
public class CartoonEnumTypeException extends EnumTypeException {

    private static final String MESSAGE = ENUM_TYPE_VALIDATION.getValue();
    private Map<String, String> validation = new ConcurrentHashMap<>();

    public CartoonEnumTypeException(Boolean isDayValid, Boolean isProgressValid, Boolean isGenreValid) {
        super(MESSAGE);
        if (!isDayValid) {
            validation.put(DAY_OF_THE_WEEK, DAY_OF_THE_WEEK_BAD_REQUEST.getValue());
        }
        if (!isProgressValid) {
            validation.put(PROGRESS, PROGRESS_BAD_REQUEST.getValue());
        }
        if (!isGenreValid) {
            validation.put(GENRE, GENRE_BAD_REQUEST.getValue());
        }
    }
}
