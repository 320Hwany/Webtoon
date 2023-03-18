package com.webtoon.member.exception;

import com.webtoon.global.error.EnumTypeException;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.webtoon.util.constant.ConstantCommon.*;
import static com.webtoon.util.enumerated.ErrorMessage.*;

@Getter
public class MemberEnumTypeException extends EnumTypeException {

    private static final String MESSAGE = ENUM_TYPE_VALIDATION.getValue();
    private Map<String, String> validation = new ConcurrentHashMap<>();

    public MemberEnumTypeException(Boolean isGenderValid) {
        super(MESSAGE);
        if (!isGenderValid) {
            validation.put(GENDER, GENDER_BAD_REQUEST.getValue());
        }
    }
}
