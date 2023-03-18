package com.webtoon.global.error;


import lombok.Getter;

import static com.webtoon.util.constant.ConstantCommon.*;

@Getter
public abstract class EnumTypeException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;
    private String message;

    public EnumTypeException(String message) {
        this.message = message;
    }
}
