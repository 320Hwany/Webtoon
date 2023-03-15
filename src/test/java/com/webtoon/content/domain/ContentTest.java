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
                .build();

        // when
        content.update(contentUpdate);

        // then
        assertThat(content.getSubTitle()).isEqualTo("수정 부제입니다");
        assertThat(content.getEpisode()).isEqualTo(30);
    }
}