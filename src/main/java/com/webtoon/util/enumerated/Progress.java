package com.webtoon.util.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Progress {

    SERIALIZATION("SERIALIZATION"),
    ONBREAK("ONBREAK"),
    COMPLETE("COMPLETE"),
    NONE("NONE");

    private String value;

    public static boolean validateValid(String inputProgress) {
        Progress[] progressList = values();
        for (Progress progress : progressList) {
            if (inputProgress.equals(progress.getValue())) {
                return true;
            }
        }
        return false;
    }
}
