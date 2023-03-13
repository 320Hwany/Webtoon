package com.webtoon.util.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DtoFieldValid {

    EMAIL("이메일을 입력해주세요");

    private String value;
}
