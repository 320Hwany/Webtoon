package com.webtoon.content.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class ContentTest extends DomainTest {

    @Test
    void getFromContentSaveAndCartoon() {
        // given
        Author author = getAuthor();
        Cartoon cartoon = getCartoon(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20)
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
}