package com.webtoon.cartoonmember.domain;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.member.domain.Member;
import com.webtoon.util.DomainTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.lang.Boolean.*;
import static org.junit.jupiter.api.Assertions.*;

class CartoonMemberTest extends DomainTest {

    @Test
    @DisplayName("thumbsUp 메소드를 실행하면 thumbsUp이 true로 바뀐다")
    void thumbsUp() {
        // given
        Cartoon cartoon = getCartoon();
        Member member = getMember();

        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(false)
                .build();

        // when
        cartoonMember.thumbsUp();

        // then
        Assertions.assertThat(cartoonMember.isThumbsUp()).isEqualTo(true);
    }

    @Test
    @DisplayName("rated 메소드를 실행하면 rated가 true로 바뀐다")
    void rated() {
        // given
        Cartoon cartoon = getCartoon();
        Member member = getMember();

        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .rated(false)
                .build();

        // when
        cartoonMember.rated();

        // then
        Assertions.assertThat(cartoonMember.isRated()).isEqualTo(true);
    }
}