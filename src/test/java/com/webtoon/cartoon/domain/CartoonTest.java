package com.webtoon.cartoon.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.util.DomainTest;
import com.webtoon.util.constant.Constant;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonTest extends DomainTest {

    @Test
    @DisplayName("CartoonSave와 Author로부터 Cartoon을 생성합니다")
    void getFromCartoonSaveAndAuthor() {
        // given
        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        Author author = getAuthor();

        // when
        Cartoon cartoon = Cartoon.getFromCartoonSaveAndAuthor(cartoonSave, author);

        // then
        assertThat(cartoon.getAuthor()).isEqualTo(author);
        assertThat(cartoon.getTitle()).isEqualTo(cartoonSave.getTitle());
        assertThat(cartoon.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.MON);
        assertThat(cartoon.getProgress()).isEqualTo(Progress.SERIALIZATION);
        assertThat(cartoon.getGenre()).isEqualTo(Genre.ROMANCE);
    }


    @Test
    @DisplayName("만화 정보가 수정됩니다")
    void update() {
        // given
        Cartoon cartoon = getCartoon();

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ACTION")
                .build();

        // when
        cartoon.update(cartoonUpdate);

        // then
        assertThat(cartoon.getTitle()).isEqualTo("수정 만화 제목");
        assertThat(cartoon.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.TUE);
        assertThat(cartoon.getProgress()).isEqualTo(Progress.COMPLETE);
        assertThat(cartoon.getGenre()).isEqualTo(Genre.ACTION);
    }

    @Test
    @DisplayName("소수 둘째 자리까지 평점을 매깁니다")
    void rating() {
        // given
        Cartoon cartoon = Cartoon.builder()
                .title("만화 제목")
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .rating(Constant.ZERO_OF_TYPE_FLOAT)
                .build();

        Float rating = 9.825F;

        // when
        cartoon.rating(rating);

        // then
        assertThat(cartoon.getRating()).isEqualTo(9.83F);
    }

    @Test
    @DisplayName("작가의 만화이면 메소드를 통과합니다")
    void checkAuthorityForCartoon200() {
        // given
        Author author = getAuthor();
        Cartoon cartoon = Cartoon.builder()
                .author(author)
                .title("만화 제목")
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .build();

        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        // expected
        cartoon.validateAuthorityForCartoon(authorSession);
    }


    @Test
    @DisplayName("작가의 만화가 아니면 접근 권한 예외가 발생합니다")
    void checkAuthorityForCartoon403() {
        // given
        Author author = getAuthor();
        Cartoon cartoon = Cartoon.builder()
                .author(author)
                .title("만화 제목")
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .build();

        AuthorSession anotherAuthorSession = AuthorSession.builder()
                .id(9999L)
                .nickName("다른 작가")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        // expected
        assertThrows(CartoonForbiddenException.class,
                () -> cartoon.validateAuthorityForCartoon(anotherAuthorSession));
    }

    @Test
    @DisplayName("요일, 진행 상황이 등록된 Enum과 일치하면 메소드가 통과합니다")
    void checkEnumTypeValid200() {
        // given
        CartoonEnumField cartoonEnumField = CartoonEnumField.builder()
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        // expected
        Cartoon.validateEnumTypeValid(cartoonEnumField);
    }

    @Test
    @DisplayName("요일, 진행 상황이 등록된 Enum 과 일치하지 않으면 예외가 발생합니다")
    void checkEnumTypeValid400() {
        // given
        CartoonEnumField cartoonEnumField = CartoonEnumField.builder()
                .dayOfTheWeek("조건에 맞지 않은 요일")
                .progress("조건에 맞지 않은 진행상황")
                .genre("조건에 맞지 않은 장르")
                .build();

        // expected
        assertThrows(EnumTypeValidException.class,
                () -> Cartoon.validateEnumTypeValid(cartoonEnumField));
    }

    @Test
    @DisplayName("요일이 등록된 리스트에 있다면 True 없으면 False 를 반환합니다")
    void checkDayValid() {
        // given
        String inputDayOfWeekTrue = "MON";
        String inputDayOfWeekFalse = "조건에 맞지 않은 요일";

        // when
        Boolean booleanTrue = Cartoon.validateDayValid(inputDayOfWeekTrue);
        Boolean booleanFalse = Cartoon.validateDayValid(inputDayOfWeekFalse);

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

        // when
        Boolean booleanTrue = Cartoon.validateProgressValid(inputProgressTrue);
        Boolean booleanFalse = Cartoon.validateProgressValid(inputProgressFalse);

        // then
        assertThat(booleanTrue).isTrue();
        assertThat(booleanFalse).isFalse();
    }

    @Test
    @DisplayName("장르가 등록된 리스트에 있다면 True 없으면 False 를 반환합니다")
    void checkGenreValid() {
        // given
        String inputGenreTrue = "ROMANCE";
        String inputGenreFalse = "조건에 맞지 않은 장르";

        // when
        Boolean booleanTrue = Cartoon.validateGenreValid(inputGenreTrue);
        Boolean booleanFalse = Cartoon.validateGenreValid(inputGenreFalse);

        // then
        assertThat(booleanTrue).isTrue();
        assertThat(booleanFalse).isFalse();
    }
}