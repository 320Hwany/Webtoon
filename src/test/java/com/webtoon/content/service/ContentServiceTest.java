package com.webtoon.content.service;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.constant.Constant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.TWO_WEEKS;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ContentServiceTest extends ServiceTest {

    @Autowired
    protected ContentService contentService;

    @Test
    @DisplayName("Content를 저장합니다 - 성공")
    void save200() {
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
    @DisplayName("Content를 수정합니다 - 성공")
    void update200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .registrationDate(LocalDate.of(1999, 3, 20))
                .build();

        // when
        contentService.update(content, contentUpdate);

        // then
        assertThat(content.getCartoon()).isEqualTo(cartoon);
        assertThat(content.getSubTitle()).isEqualTo("수정 부제입니다");
        assertThat(content.getEpisode()).isEqualTo(30);
        assertThat(content.getRegistrationDate()).isEqualTo(LocalDate.of(1999, 3, 20));
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
        Content content = contentService.getContentFromContentSaveAndCartoon(contentSave, cartoon);

        // then
        assertThat(content.getCartoon()).isEqualTo(cartoon);
        assertThat(content.getSubTitle()).isEqualTo("부제 입니다");
        assertThat(content.getEpisode()).isEqualTo(20L);
        assertThat(content.getRegistrationDate()).isEqualTo(LocalDate.of(2023, 1, 19));
    }

    @Test
    @DisplayName("등록 날짜로부터 2주 지난 날짜로 미리보기 가능한 날짜를 설정합니다")
    void getLockLocalDate() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        // when
        LocalDate lockLocalDate = contentService.getLockLocalDate(content, TWO_WEEKS);
        LocalDate registrationDate = content.getRegistrationDate();

        // then
        assertThat(lockLocalDate).isEqualTo(registrationDate.plusWeeks(2));
    }

    @Test
    @DisplayName("만화에 대한 에피소드가 존재하면 컨텐츠를 가져옵니다 - 성공")
    void findByCartoonAndEpisode200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        // when
        Content findContent = contentService.findByCartoonIdAndEpisode(cartoon.getId(), content.getEpisode());

        // then
        assertThat(findContent).isEqualTo(content);
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 예외를 보여줍니다 - 성공")
    void findByCartoonAndEpisode404Cartoon() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        // expected
        assertThrows(ContentNotFoundException.class,
                () -> contentService.findByCartoonIdAndEpisode(9999L, content.getEpisode()));
    }

    @Test
    @DisplayName("만화에 대한 에피소드가 존재하지 않으면 예외를 보여줍니다 - 성공")
    void findByCartoonAndEpisode404Episode() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        // expected
        assertThrows(ContentNotFoundException.class,
                () -> contentService.findByCartoonIdAndEpisode(cartoon.getId(), 9999));
    }
}