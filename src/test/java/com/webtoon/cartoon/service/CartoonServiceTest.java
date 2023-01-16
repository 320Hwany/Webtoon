package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Progress;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CartoonServiceTest extends ServiceTest {

    @Autowired
    private CartoonService cartoonService;

    @Test
    @DisplayName("로그인한 작가 계정으로 만화를 등록합니다 - 성공")
    void save() {
        // given
        Author author = saveAuthorInRepository();

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .build();

        AuthorSession authorSession = AuthorSession.builder()
                .id(author.getId())
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
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
        Assertions.assertThrows(AuthorNotFoundException.class,
                () -> cartoonService.save(cartoonSave, authorSession));
    }

    @Test
    @DisplayName("로그인한 작가라도 day, progress 가 Enum 에 없다면 만화를 등록할 수 없습니다 - 실패")
    void saveFailByValidEnum() {
        // given
        Author author = saveAuthorInRepository();

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("등록 요일 입력 잘못함")
                .progress("현재 진행상황 입력 잘못함")
                .build();

        AuthorSession authorSession = AuthorSession.builder()
                .id(author.getId())
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        // expected
        Assertions.assertThrows(EnumTypeValidException.class,
                () -> cartoonService.save(cartoonSave, authorSession));
    }
}