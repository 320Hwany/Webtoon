package com.webtoon.cartoon.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CartoonTransactionServiceTest extends ServiceTest {

    @Autowired
    private CartoonTransactionService cartoonTransactionService;

    @Test
    @DisplayName("AuthorSession과 id값이 일치하는 Author로 만화를 등록합니다 - 성공")
    void saveSet200() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        // when
        cartoonTransactionService.saveSet(cartoonSave, authorSession);

        // then
        assertThat(cartoonRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("AuthorSession과 id값이 일치하는 Author가 없다면 만화 등록을 할 수 없습니다 - 실패")
    void saveSet404() {
        // given
        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        AuthorSession authorSession = AuthorSession.builder()
                .id(1L)
                .nickname("DB에 없는 회원")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(AuthorNotFoundException.class,
                () -> cartoonTransactionService.saveSet(cartoonSave, authorSession));
    }

    @Test
    @DisplayName("cartoonId와 일치하는 만화가 존재하면 수정에 성공합니다")
    @Transactional
    void updateSet200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();
        // when
        cartoonTransactionService.updateSet(cartoonUpdate, cartoon.getId());

        // then
        assertThat(cartoon.getTitle()).isEqualTo("수정 만화 제목");
        assertThat(cartoon.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.TUE);
        assertThat(cartoon.getProgress()).isEqualTo(Progress.COMPLETE);
        assertThat(cartoon.getGenre()).isEqualTo(Genre.ROMANCE);
    }

    @Test
    @DisplayName("cartoonId와 일치하는 만화가 없다면 예외가 발생합니다")
    @Transactional
    void updateSet404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonTransactionService.updateSet(cartoonUpdate,9999L));
    }

    @Test
    @DisplayName("cartoonId와 일치하는 만화가 존재하면 만화를 삭제합니다")
    void deleteSet200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        // when
        cartoonTransactionService.deleteSet(cartoon.getId());

        // then
        assertThat(cartoonRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 예외가 발생합니다")
    void deleteSet404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonTransactionService.deleteSet(9999L));
    }

}