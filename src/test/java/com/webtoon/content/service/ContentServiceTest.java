package com.webtoon.content.service;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.util.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ContentServiceTest extends ServiceTest {

    @Autowired
    protected ContentService contentService;

    @Test
    @DisplayName("Content를 저장합니다 - 성공")
    void save() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        Content content = Content.builder()
                .cartoon(cartoon)
                .subTitle("부제입니다")
                .episode(20)
                .rating(9.8f)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        // when
        Content afterSaveContent = contentService.save(content);

        // then
        assertThat(afterSaveContent).isEqualTo(content);
    }

    @Test
    @DisplayName("존재하는 만화라면 getContentFromContentSaveAndCartoonId로 가져옵니다 - 성공")
    void getContentFromContentSaveAndCartoonId200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        // when
        Content content = contentService.getContentFromContentSaveAndCartoonId(contentSave, cartoon.getId());

        // then
        assertThat(content.getCartoon()).isEqualTo(cartoon);
        assertThat(content.getSubTitle()).isEqualTo("부제 입니다");
        assertThat(content.getEpisode()).isEqualTo(20);
        assertThat(content.getRating()).isEqualTo(0f);
        assertThat(content.getRegistrationDate()).isEqualTo(LocalDate.of(2023, 1, 19));
    }

    @Test
    @DisplayName("존재하지 않는 만화라면 getContentFromContentSaveAndCartoonId로 가져올 수 없습니다 - 실패")
    void getContentFromContentSaveAndCartoonId404() {
        // given
        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> contentService.getContentFromContentSaveAndCartoonId(contentSave, 9999L));
    }
}