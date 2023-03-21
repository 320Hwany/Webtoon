package com.webtoon.global.error;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import static com.webtoon.util.constant.ConstantCommon.BAD_REQUEST;

@Getter
public class BindingException extends RuntimeException {

    private final String statusCode = BAD_REQUEST;

    private BindingResult bindingResult;

    public BindingException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public static void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BindingException(bindingResult);
        }
    }
}
