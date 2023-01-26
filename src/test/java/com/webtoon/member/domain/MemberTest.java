package com.webtoon.member.domain;

import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest extends DomainTest {

    @Test
    void update() {
        // given
        Member member = getMember();

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickName("수정 회원 닉네임")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        // when
        member.update(memberUpdate);

        // then
        assertThat(member.getNickName()).isEqualTo(memberUpdate.getNickName());
        assertThat(member.getEmail()).isEqualTo(memberUpdate.getEmail());
        assertThat(member.getPassword()).isEqualTo(memberUpdate.getPassword());
    }

    @Test
    void chargeCoin() {
        // given
        Member member = getMember();

        // when
        member.chargeCoin(10000);

        // then
        assertThat(member.getCoin()).isEqualTo(10000);
    }
}
