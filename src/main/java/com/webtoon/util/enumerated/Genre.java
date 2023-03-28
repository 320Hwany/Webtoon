package com.webtoon.util.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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
        return Arrays.stream(genreList).anyMatch(genre -> inputGenre.equals(genre.getValue()));
    }
}
