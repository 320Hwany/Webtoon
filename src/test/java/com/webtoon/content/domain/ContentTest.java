package com.webtoon.content.domain;

import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.util.DomainTest;
import com.webtoon.util.constant.ConstantCommon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.webtoon.util.constant.ConstantCommon.*;
import static org.assertj.core.api.Assertions.*;

class ContentTest extends DomainTest {

    @Test
    @DisplayName("컨텐츠를 수정합니다")
    void update() {
        // given
        Content content = getContent();

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .build();

        // when
        content.update(contentUpdate);

        // then
        assertThat(content.getSubTitle()).isEqualTo("수정 부제입니다");
        assertThat(content.getEpisode()).isEqualTo(30);
    }

    @Test
    @DisplayName("등록 날짜로부터 미리 보기 잠금 날짜를 설정합니다")
    void getLockLocalDate() {
        // given
        Content content = Content.builder()
                .registrationDate(LocalDate.now())
                .build();

        // when
        LocalDate lockLocalDate = content.getLockLocalDate(TWO_WEEKS);
        LocalDate registrationDate = content.getRegistrationDate();

        // then
        assertThat(lockLocalDate).isEqualTo(registrationDate.plusWeeks(TWO_WEEKS));
    }
}