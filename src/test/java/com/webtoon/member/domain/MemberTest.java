package com.webtoon.member.domain;

import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.PAYCOIN;
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
        member.update(memberUpdate, passwordEncoder);

        // then
        assertThat(member.getNickName()).isEqualTo(memberUpdate.getNickName());
        assertThat(member.getEmail()).isEqualTo(memberUpdate.getEmail());
        assertThat(passwordEncoder.matches("123456789", member.getPassword())).isTrue();
    }

    @Test
    void chargeCoin() {
        // given
        Member member = getMember();

        // when
        member.chargeCoin(10000L);

        // then
        assertThat(member.getCoin()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("현재 날짜가 미리보기 잠금 날짜를 지났다면 코인을 지불하지 않습니다")
    void validatePreviewContentAfterNowDate() {
        // given
        Member member = getMember();
        member.chargeCoin(10000L);
        LocalDate lockLocalDate = LocalDate.now().minusWeeks(1);

        // when
        member.validatePreviewContent(lockLocalDate);

        // then
        assertThat(member.getCoin()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("현재 날짜가 미리보기 잠금 날짜를 지나지 않았다면 코인을 지불합니다")
    void validatePreviewContentBeforeNowDate() {
        // given
        Member member = getMember();
        member.chargeCoin(10000L);
        LocalDate lockLocalDate = LocalDate.now().plusWeeks(1);

        // when
        member.validatePreviewContent(lockLocalDate);

        // then
        assertThat(member.getCoin()).isEqualTo(10000L - PAYCOIN);
    }

    @Test
    @DisplayName("코인이 부족하면 미리보기 기능을 이용할 수 없습니다")
    void LockOfCoin() {
        // given
        Member member = getMember();
        member.chargeCoin(0L);
        LocalDate lockLocalDate = LocalDate.now().plusWeeks(1);

        // expected
        Assertions.assertThrows(LackOfCoinException.class,
                () -> member.validatePreviewContent(lockLocalDate));
    }
}
