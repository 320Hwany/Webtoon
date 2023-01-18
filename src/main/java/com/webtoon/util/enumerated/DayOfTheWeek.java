package com.webtoon.util.enumerated;


public enum DayOfTheWeek implements EnumModel {

    MON("MON"),
    TUE("TUE"),
    WED("WED"),
    THU("THU"),
    FRI("FRI"),
    SAT("SAT"),
    SUN("SUN"),
    NONE(null);

    private String value;

    DayOfTheWeek(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
