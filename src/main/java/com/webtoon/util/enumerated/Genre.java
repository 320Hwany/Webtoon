package com.webtoon.util.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genre {

    COMEDY("COMEDY"),
    ROMANCE("ROMANCE"),
    FANTASY("FANTASY"),
    THRILLER("THRILLER"),
    SPORTS("SPORTS"),
    ACTION("ACTION"),
    NONE("NONE");

    private String value;

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
