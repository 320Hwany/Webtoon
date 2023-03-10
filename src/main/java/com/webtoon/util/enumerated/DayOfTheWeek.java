package com.webtoon.util.enumerated;


import lombok.AllArgsConstructor;
import lombok.Getter;

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
        for (DayOfTheWeek day : DayList) {
            if (inputDayOfWeek.equals(day.getValue())) {
                return true;
            }
        }
        return false;
    }
}
