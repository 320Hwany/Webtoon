package com.webtoon.cartoon.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EnumTypeValidException extends RuntimeException {

    private final String statusCode = "400";
    private static final String MESSAGE = "잘못된 요청입니다";

    private Map<String, String> validation = new HashMap<>();

    public EnumTypeValidException(Boolean isDayValid, Boolean isProgressValid, Boolean isGenreValid) {
        super(MESSAGE);
        if (isDayValid == false) {
            validation.put("dayOfTheWeek", "요일을 잘못 입력하였습니다");
        }
        if (isProgressValid == false) {
            validation.put("Progress", "현재 진행 상황을 잘못 입력하였습니다");
        }
        if (isProgressValid == false) {
            validation.put("Genre", "장르를 잘못 입력하였습니다");
        }
    }
}
