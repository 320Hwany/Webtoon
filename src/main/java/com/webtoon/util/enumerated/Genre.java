package com.webtoon.util.enumerated;

public enum Genre implements EnumModel {

    COMEDY("COMEDY"),
    ROMANCE("ROMANCE"),
    FANTASY("FANTASY"),
    THRILLER("THRILLER"),
    SPORTS("SPORTS"),
    ACTION("ACTION"),
    NONE(null);

    private String value;

    Genre(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static boolean validateValid(String inputGenre) {
        Genre[] genreList = values();
        for (Genre genre : genreList) {
            if (inputGenre.equals(genre.getValue())) {
                return true;
            }
        }
        return false;
    }
}
