package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.global.error.ForbiddenException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonServiceTest extends ServiceTest {

    @Autowired
    private CartoonService cartoonService;

    @Test
    @DisplayName("로그인한 작가 계정으로 만화를 등록합니다 - 성공")
    void save() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .build();

        // when
        Cartoon cartoon = cartoonService.save(cartoonSave, authorSession);

        // then
        assertThat(cartoon.getTitle()).isEqualTo("만화 제목");
        assertThat(cartoon.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.MON);
        assertThat(cartoon.getProgress()).isEqualTo(Progress.SERIALIZATION);
        assertThat(cartoon.getAuthor()).isEqualTo(author);
    }

    @Test
    @DisplayName("로그인한 작가 계정이 없다면 만화 등록을 할 수 없습니다 - 실패")
    void saveFailByNotFoundAuthor() {
        // given
        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .build();

        AuthorSession authorSession = AuthorSession.builder()
                .id(1L)
                .nickName("DB에 없는 회원")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(AuthorNotFoundException.class,
                () -> cartoonService.save(cartoonSave, authorSession));
    }

    @Test
    @DisplayName("로그인한 작가라도 day, progress 가 Enum 에 없다면 만화를 등록할 수 없습니다 - 실패")
    void saveFailByValidEnum() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("등록 요일 입력 잘못함")
                .progress("현재 진행상황 입력 잘못함")
                .build();


        // expected
        assertThrows(EnumTypeValidException.class,
                () -> cartoonService.save(cartoonSave, authorSession));
    }

    @Test
    @DisplayName("작가, 만화가 모두 존재하고 권한 접근이 있다면 만화를 수정할 수 있습니다 - 성공")
    void updateSuccess() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .build();

        // when
        Cartoon afterUpdate = cartoonService.update(cartoonUpdate, cartoon.getId(), authorSession);

        // then
        assertThat(afterUpdate.getId()).isEqualTo(cartoon.getId());
        assertThat(afterUpdate.getTitle()).isEqualTo("수정 만화 제목");
        assertThat(afterUpdate.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.TUE);
        assertThat(afterUpdate.getProgress()).isEqualTo(Progress.COMPLETE);
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 수정할 수 없습니다 - 실패")
    void updateFailByNotFoundCartoon() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .build();

        // when
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.update(cartoonUpdate, 9999L, authorSession));
    }

    @Test
    @DisplayName("만화가 존재해도 수정 조건이 맞지 않으면 수정할 수 없습니다 - 실패")
    void updateFailByValid() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("조건에 맞지 않은 요일")
                .progress("조건에 맞지 않은 진행 상황")
                .build();

        // when
        assertThrows(EnumTypeValidException.class,
                () -> cartoonService.update(cartoonUpdate, cartoon.getId(), authorSession));
    }

    @Test
    @DisplayName("작가가 만화에 접근 권한이 없으면 수정할 수 없습니다 - 실패")
    void updateFailByForbidden() {
        // given
        Author author = saveAuthorInRepository();

        Author anotherAuthor = Author.builder()
                .nickName("다른 작가")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        authorRepository.save(anotherAuthor);
        Cartoon cartoon = saveCartoonInRepository(anotherAuthor);
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .build();

        // when
        assertThrows(ForbiddenException.class,
                () -> cartoonService.update(cartoonUpdate, cartoon.getId(), authorSession));
    }
}