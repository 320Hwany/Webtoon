package com.webtoon.util.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    MAN("MAN"),
    WOMAN("WOMAN");

    private String value;

    public static boolean validateValid(String inputGender) {
        Gender[] genders = values();
        for (Gender gender : genders) {
            if (inputGender.equals(gender.getValue())) {
                return true;
            }
        }
        return false;
    }
}
