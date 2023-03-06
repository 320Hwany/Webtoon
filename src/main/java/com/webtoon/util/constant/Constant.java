package com.webtoon.util.constant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class Constant {

    private Constant() {}

    public static final long PAYCOIN = 200L;
    public static final long TWO_WEEKS = 2L;
    public static final double ZERO_OF_TYPE_DOUBLE = 0;
    public static final long ZERO_OF_TYPE_LONG = 0L;

    public static final PageRequest firstPage
            = PageRequest.of(0, 20, Sort.Direction.DESC, "id");
}
