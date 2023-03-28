package com.webtoon.util.enumerated;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DayOfTheWeek {

    MON("MON"),
    TUE("TUE"),
    WED("WED"),
    THU("THU"),
    FRI("FRI"),
    SAT("SAT"),
    SUN("SUN"),
    NONE("NONE");

    private String value;

    public static boolean validateValid(String inputDayOfWeek) {
        DayOfTheWeek[] DayList = values();
        return Arrays.stream(DayList).anyMatch(day -> inputDayOfWeek.equals(day.getValue()));
    }
}
