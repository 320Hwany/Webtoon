package com.webtoon.util.enumerated;

public enum Genre implements EnumModel {

    COMEDY("COMEDY"),
    ROMANCE("ROMANCE"),
    FANTASY("FANTASY"),
    THRILLER("THRILLER"),
    SPORTS("SPORTS"),
    ACTION("ACTION");

    private String value;

    Genre(String value) {
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
