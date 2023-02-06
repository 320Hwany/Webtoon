package com.webtoon.content.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.util.DomainTest;
import com.webtoon.util.constant.Constant;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.*;
import static org.assertj.core.api.Assertions.*;

class ContentTest extends DomainTest {

    @Test
    void getFromContentSaveAndCartoon() {
        // given
        Cartoon cartoon = getCartoon();

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20L)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        // when
        Content content = Content.getFromContentSaveAndCartoon(contentSave, cartoon);

        // then
        assertThat(content.getCartoon()).isEqualTo(cartoon);
        assertThat(content.getSubTitle()).isEqualTo("부제 입니다");
        assertThat(content.getEpisode()).isEqualTo(20);
        assertThat(content.getRegistrationDate()).isEqualTo(LocalDate.of(2023, 1, 19));
    }

    @Test
    void update() {
        // given
        Content content = getContent();

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30L)
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