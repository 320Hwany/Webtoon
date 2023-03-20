package com.webtoon.cartoon.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CartoonSearchTest {

    @Test
    @DisplayName("입력한 페이지로 offset을 구합니다")
    void getOffset() {
        // given
        CartoonSearch cartoonSearch = CartoonSearch.builder()
                .page(2)
                .build();

        // when
        int offset = cartoonSearch.getOffset();

        // then
        assertThat(offset).isEqualTo(20);
    }
}