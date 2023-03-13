package com.webtoon.content.domain;

import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.webtoon.util.constant.ConstantCommon.*;
import static org.assertj.core.api.Assertions.*;

class ContentTest extends DomainTest {

    @Test
    void update() {
        // given
        Content content = getContent();

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .registrationDate(LocalDate.of(1999, 3, 20))
                .build();

        // when
        content.update(contentUpdate);

        // then
        assertThat(content.getSubTitle()).isEqualTo("수정 부제입니다");
        assertThat(content.getEpisode()).isEqualTo(30);
        assertThat(content.getRegistrationDate()).isEqualTo(LocalDate.of(1999, 3, 20));
    }

    @Test
    void getLockLocalDate() {
        // given
        Content content = getContent();

        // when
        LocalDate lockLocalDate = content.getLockLocalDate(TWO_WEEKS);
        LocalDate registrationDate = content.getRegistrationDate();

        // then
        assertThat(lockLocalDate).isEqualTo(registrationDate.plusWeeks(2));
    }
}