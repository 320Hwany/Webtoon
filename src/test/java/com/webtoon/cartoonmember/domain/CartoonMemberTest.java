package com.webtoon.cartoonmember.domain;

import com.webtoon.author.domain.Author;
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
    @DisplayName("사용자가 좋아요를 누르면 CartoonMember 연결 테이블의 thumbsUp이 True로 바뀐다")
    void thumbsUp() {
        // given
        Author author = getAuthor();
        Cartoon cartoon = getCartoon(author);
        Member member = getMember();

        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(FALSE)
                .build();

        // when
        cartoonMember.thumbsUp();

        // then
        Assertions.assertThat(cartoonMember.getThumbsUp()).isEqualTo(TRUE);
    }
}