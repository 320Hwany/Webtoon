package com.webtoon.util.constant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ConstantCommon {

    private ConstantCommon() {}

    public static final long PAYCOIN = 200L;
    public static final long TWO_WEEKS = 2L;
    public static final double ZERO_OF_TYPE_DOUBLE = 0;

    public static final long ZERO_OF_TYPE_LONG = 0L;

    public static final String DAY_OF_THE_WEEk = "DayOfTheWeek";
    public static final String PROGRESS = "Progress";
    public static final String GENRE = "Genre";
    public static final String BAD_REQUEST = "400";
    public static final String UNAUTHORIZED = "401";
    public static final String FORBIDDEN = "403";
    public static final String NOT_FOUND = "404";

    public static final PageRequest firstPage
            = PageRequest.of(0, 20, Sort.Direction.DESC, "id");
}
