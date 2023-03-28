package com.webtoon.util.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {

    MAN("MAN"),
    WOMAN("WOMAN");

    private String value;

    public static boolean validateValid(String inputGender) {
        Gender[] genders = values();
        return Arrays.stream(genders).anyMatch(gender -> inputGender.equals(gender.getValue()));
    }
}
