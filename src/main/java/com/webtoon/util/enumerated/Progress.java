package com.webtoon.util.enumerated;


public enum Progress implements EnumModel{

    SERIALIZATION("SERIALIZATION"),
    ONBREAK("ONBREAK"),
    COMPLETE("COMPLETE"),
    NONE(null);

    private String value;

    Progress(String value) {
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
