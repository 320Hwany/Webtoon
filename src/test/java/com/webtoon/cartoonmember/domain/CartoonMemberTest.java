package com.webtoon.cartoonmember.domain;

import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CartoonMemberTest extends DomainTest {

    @Test
    @DisplayName("thumbsUp 메소드를 실행하면 thumbsUp이 true로 바뀐다")
    void thumbsUp() {
        // given
        CartoonMember cartoonMember = CartoonMember.builder()
                .thumbsUp(false)
                .build();

        // when
        cartoonMember.thumbsUp();

        // then
        assertThat(cartoonMember.isThumbsUp()).isEqualTo(true);
    }

    @Test
    @DisplayName("rated 메소드를 실행하면 rated가 true로 바뀐다")
    void rated() {
        // given
        CartoonMember cartoonMember = CartoonMember.builder()
                .rated(false)
                .build();

        // when
        cartoonMember.rated();

        // then
        assertThat(cartoonMember.isRated()).isEqualTo(true);
    }

    @Test
    @DisplayName("읽은 날짜를 업데이트합니다")
    void updateReadDate() {
        // given
        CartoonMember cartoonMember = CartoonMember.builder()
                .rated(false)
                .build();

        LocalDateTime time = LocalDateTime.of(1999, 03, 20, 12, 00);
        // when
        cartoonMember.updateReadDate(time);

        // then
        assertThat(cartoonMember.getLastReadDate()).isEqualTo(time);
    }
}