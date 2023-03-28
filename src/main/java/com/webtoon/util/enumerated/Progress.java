package com.webtoon.util.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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
        return Arrays.stream(progressList).anyMatch(progress -> inputProgress.equals(progress.getValue()));
    }
}
