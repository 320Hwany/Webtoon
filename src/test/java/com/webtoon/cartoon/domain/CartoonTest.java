package com.webtoon.cartoon.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.global.error.ForbiddenException;
import com.webtoon.util.DomainTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonTest extends DomainTest {

    @Test
    @DisplayName("만화 정보가 수정됩니다 - 성공")
    void update() {
        // given
        Author author = getAuthor();
        Cartoon cartoon = getCartoon(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .build();

        // when
        cartoon.update(cartoonUpdate);

        // then
        assertThat(cartoon.getTitle()).isEqualTo("수정 만화 제목");
        assertThat(cartoon.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.TUE);
        assertThat(cartoon.getProgress()).isEqualTo(Progress.COMPLETE);
    }

    @Test
    @DisplayName("작가의 만화가 아니면 접근 권한 예외가 발생합니다")
    void checkAuthorityForCartoon() {
        // given
        Author author = getAuthor();
        Cartoon cartoon = getCartoon(author);
        AuthorSession anotherAuthorSession = AuthorSession.builder()
                .nickName("다른 작가")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        // expected
        assertThrows(ForbiddenException.class,
                () -> cartoon.checkAuthorityForCartoon(anotherAuthorSession));
    }

    @Test
    @DisplayName("요일, 진행 상황이 등록된 Enum 과 일치하지 않으면 예외가 발생합니다")
    void checkEnumTypeValid() {
        // given
        String inputDayOfWeek = "조건에 맞지 않은 요일";
        String inputProgress = "조건에 맞지 않은 진행상황";

        // expected
        assertThrows(EnumTypeValidException.class,
                () -> Cartoon.checkEnumTypeValid(inputDayOfWeek, inputProgress));
    }

    @Test
    @DisplayName("요일이 등록된 리스트에 있다면 True 없으면 False 를 반환합니다")
    void checkDayValid() {
        // given
        String inputDayOfWeekTrue = "MON";
        String inputDayOfWeekFalse = "조건에 맞지 않은 요일";
        DayOfTheWeek[] DayList = DayOfTheWeek.values();

        // when
        Boolean booleanTrue = Cartoon.checkDayValid(inputDayOfWeekTrue, DayList);
        Boolean booleanFalse = Cartoon.checkDayValid(inputDayOfWeekFalse, DayList);

        // then
        assertThat(booleanTrue).isTrue();
        assertThat(booleanFalse).isFalse();
    }

    @Test
    @DisplayName("진행상황이 등록된 리스트에 있다면 True 없으면 False 를 반환합니다")
    void checkProgressValid() {
        // given
        String inputProgressTrue = "SERIALIZATION";
        String inputProgressFalse = "조건에 맞지 않은 진행상황";
        Progress[] progressList = Progress.values();

        // when
        Boolean booleanTrue = Cartoon.checkProgressValid(inputProgressTrue, progressList);
        Boolean booleanFalse = Cartoon.checkProgressValid(inputProgressFalse, progressList);

        // then
        assertThat(booleanTrue).isTrue();
        assertThat(booleanFalse).isFalse();
    }
}