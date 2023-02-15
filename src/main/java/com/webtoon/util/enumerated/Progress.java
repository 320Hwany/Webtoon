package com.webtoon.util.enumerated;


public enum Progress implements EnumModel {

    SERIALIZATION("SERIALIZATION"),
    ONBREAK("ONBREAK"),
    COMPLETE("COMPLETE"),
    NONE("NONE");

    private String value;

    Progress(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

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
