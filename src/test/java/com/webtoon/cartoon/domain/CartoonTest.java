package com.webtoon.cartoon.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.CartoonEnumTypeException;
import com.webtoon.util.DomainTest;
import com.webtoon.util.constant.ConstantCommon;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.webtoon.util.constant.ConstantCommon.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonTest extends DomainTest {

    @Test
    @DisplayName("만화 타입이 Enum 타입과 맞으면 메소드를 통과합니다")
    void validateEnumTypeValid200() {
        // given
        String dayOfTheWeek = "MON";
        String progress = "SERIALIZATION";
        String genre = "ROMANCE";

        CartoonEnumField cartoonEnumField = CartoonEnumField.builder()
                .dayOfTheWeek(dayOfTheWeek)
                .progress(progress)
                .genre(genre)
                .build();

        // expected
        Cartoon.validateEnumTypeValid(cartoonEnumField);
    }

    @Test
    @DisplayName("요일, 진행 상황이 등록된 Enum 과 일치하지 않으면 예외가 발생합니다")
    void validateEnumTypeValid400() {
        // given
        CartoonEnumField cartoonEnumField = CartoonEnumField.builder()
                .dayOfTheWeek("조건에 맞지 않은 요일")
                .progress("조건에 맞지 않은 진행상황")
                .genre("조건에 맞지 않은 장르")
                .build();

        // expected
        assertThrows(CartoonEnumTypeException.class,
                () -> Cartoon.validateEnumTypeValid(cartoonEnumField));
    }

    @Test
    @DisplayName("요일이 등록된 리스트에 있다면 True 없으면 False 를 반환합니다")
    void validateDayValid() {
        // given
        String inputDayOfWeekTrue = "MON";
        String inputDayOfWeekFalse = "조건에 맞지 않은 요일";

        // when
        Boolean booleanTrue = DayOfTheWeek.validateValid(inputDayOfWeekTrue);
        Boolean booleanFalse = DayOfTheWeek.validateValid(inputDayOfWeekFalse);

        // then
        assertThat(booleanTrue).isTrue();
        assertThat(booleanFalse).isFalse();
    }

    @Test
    @DisplayName("진행상황이 등록된 리스트에 있다면 True 없으면 False 를 반환합니다")
    void validateProgressValid() {
        // given
        String inputProgressTrue = "SERIALIZATION";
        String inputProgressFalse = "조건에 맞지 않은 진행상황";

        // when
        Boolean booleanTrue = Progress.validateValid(inputProgressTrue);
        Boolean booleanFalse = Progress.validateValid(inputProgressFalse);

        // then
        assertThat(booleanTrue).isTrue();
        assertThat(booleanFalse).isFalse();
    }

    @Test
    @DisplayName("장르가 등록된 리스트에 있다면 True 없으면 False 를 반환합니다")
    void validateGenreValid() {
        // given
        String inputGenreTrue = "ROMANCE";
        String inputGenreFalse = "조건에 맞지 않은 장르";

        // when
        Boolean booleanTrue = Genre.validateValid(inputGenreTrue);
        Boolean booleanFalse = Genre.validateValid(inputGenreFalse);

        // then
        assertThat(booleanTrue).isTrue();
        assertThat(booleanFalse).isFalse();
    }

    @Test
    @DisplayName("만화에 대한 작가 정보가 일치하면 메소드를 통과합니다")
    void validateAuthorityForCartoon200() {
        // given
        Author author = Author.builder()
                .nickname("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();
        ReflectionTestUtils.setField(author, "id", 1L);

        Cartoon cartoon = Cartoon.builder()
                .author(author)
                .build();

        AuthorSession authorSession = AuthorSession.builder()
                .id(author.getId())
                .nickname(author.getNickname())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        // expected
        cartoon.validateAuthorityForCartoon(authorSession);
    }

    @Test
    @DisplayName("만화에 대한 작가 정보가 없거나 다른 작가라면 예외가 발생합니다")
    void validateAuthorityForCartoon403() {
        // given
        Author author = getAuthor();
        Cartoon cartoon = getCartoon();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        // expected
        assertThrows(CartoonForbiddenException.class,
                () -> cartoon.validateAuthorityForCartoon(authorSession));
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
    @DisplayName("좋아요 수가 1 증가합니다")
    void addLike() {
        // given
        Cartoon cartoon = Cartoon.builder()
                .title("만화 제목")
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .rating(ZERO_OF_TYPE_DOUBLE)
                .likes(10)
                .build();

        // when
        cartoon.addLike();

        // then
        assertThat(cartoon.getLikes()).isEqualTo(11);
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
                .rating(ZERO_OF_TYPE_DOUBLE)
                .build();

        double rating1 = 9.8253;
        double rating2 = 9.9321;

        // when 1
        cartoon.rating(rating1, 0);

        // then 1
        assertThat(cartoon.getRating()).isEqualTo(9.83);

        // when 1
        cartoon.rating(rating2, 1);

        // then 1
        assertThat(cartoon.getRating()).isEqualTo(9.88);
    }

    @Test
    @DisplayName("만화 리스트의 수로 평점의 합을 구합니다")
    void calculateSum() {
        // given
        Cartoon cartoon = Cartoon.builder()
                .title("만화 제목")
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .rating(9.8)
                .build();
        long cartoonListSize = 10;

        // when
        double sum = cartoon.calculateSum(cartoonListSize);

        // then
        assertThat(sum).isEqualTo(98);
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
}