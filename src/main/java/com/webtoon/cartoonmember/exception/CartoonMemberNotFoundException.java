package com.webtoon.cartoonmember.exception;

import com.webtoon.global.error.NotFoundException;

public class CartoonMemberNotFoundException extends NotFoundException {

    private static final String MESSAGE = "만화 - 회원 연결 정보를 찾을 수 없습니다";

    public CartoonMemberNotFoundException() {
        super(MESSAGE);
    }
}
