package com.webtoon.cartoon.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.*;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.exception.CartoonEnumTypeException;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonServiceTest extends ServiceTest {

    @Autowired
    private CartoonService cartoonService;

    @Test
    @DisplayName("세션과 id값이 일치하는 작가로 만화를 등록합니다 - 성공")
    void save200() {
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
        cartoonService.save(cartoonSave, authorSession);

        // then
        assertThat(cartoonRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("세션과 id값이 일치하는 작가가 없다면 만화 등록을 할 수 없습니다 - 실패")
    void save404() {
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
                () -> cartoonService.save(cartoonSave, authorSession));
    }

    @Test
    @DisplayName("만화 입력 조건 중 Enum 타입이 유효하면 메소드를 통과합니다")
    void validateCartoonSave200() {
        // given
        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .genre("ROMANCE")
                .progress("SERIALIZATION")
                .build();

        // expected
        CartoonService.validateCartoonSave(cartoonSave);
    }

    @Test
    @DisplayName("만화 입력 조건 중 Enum 타입이 유효하지 않으면 예외가 발생합니다")
    void validateCartoonSave400() {
        // given
        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("유효하지 않은 날짜")
                .genre("유효하지 않은 장르")
                .progress("유효하지 않은 진행상황")
                .build();

        // expected
        assertThrows(CartoonEnumTypeException.class,
                () -> CartoonService.validateCartoonSave(cartoonSave));
    }

    @Test
    @DisplayName("제목을 포함하는 만화가 있다면 한 페이지를 보여줍니다 - 성공")
    void findAllByTitle200() {
        // given 1
        Author author = saveAuthorInRepository();

        CartoonSearchTitle cartoonSearchTitle = CartoonSearchTitle.builder()
                .page(1)
                .size(20)
                .title("만화 제목")
                .build();

        // given 2 - cartoonList
        List<Cartoon> cartoonListKr = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonListEn = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("cartoon title " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonListKr);
        cartoonRepository.saveAll(cartoonListEn);

        // when
        List<CartoonResponse> cartoonResponseList = cartoonService.findAllByTitle(cartoonSearchTitle);

        // then
        assertThat(cartoonResponseList.size()).isEqualTo(10);
        assertThat(cartoonResponseList.get(0).getTitle()).isEqualTo("만화 제목 10");
    }

    @Test
    @DisplayName("입력한 요일에 맞는 만화를 좋아요 많은 순으로 보여줍니다 - 성공")
    void findAllByDayOfTheWeek() {
        // given 1
        Author author = saveAuthorInRepository();
        CartoonSearchCond cartoonSearchDto = CartoonSearchCond.builder()
                .page(1)
                .dayOfTheWeek("MON")
                .build();

        // given 2 - cartoonList
        List<Cartoon> cartoonMonList = LongStream.range(1, 21)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .likes(i)
                        .dayOfTheWeek(DayOfTheWeek.MON)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonTueList = LongStream.range(21, 31)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .likes(i)
                        .dayOfTheWeek(DayOfTheWeek.TUE)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonMonList);
        cartoonRepository.saveAll(cartoonTueList);

        // when
        List<CartoonResponse> cartoonResponseList
                = cartoonService.findAllByCartoonCondOrderByLikes(cartoonSearchDto);

        // then
        assertThat(cartoonResponseList.size()).isEqualTo(20);
        assertThat(cartoonResponseList.get(0).getLikes()).isEqualTo(20);
    }

    @Test
    @DisplayName("만화 상태와 일치하는 만화 리스트를 한 페이지 가져옵니다 - 성공")
    void findAllByProgress200() {
        // given 1
        Author author = saveAuthorInRepository();
        CartoonSearchCond cartoonSearchDto = CartoonSearchCond.builder()
                .page(1)
                .progress("SERIALIZATION")
                .build();

        // given 2 - cartoonList
        List<Cartoon> cartoonProgressSerializationList = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .progress(Progress.SERIALIZATION)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonProgressCompleteList = IntStream.range(11, 16)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .progress(Progress.COMPLETE)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonProgressSerializationList);
        cartoonRepository.saveAll(cartoonProgressCompleteList);

        // when
        List<CartoonResponse> cartoonResponseList
                = cartoonService.findAllByCartoonCondOrderByLikes(cartoonSearchDto);

        // then
        assertThat(cartoonResponseList.size()).isEqualTo(10);
        assertThat(cartoonResponseList.get(0).getLikes()).isEqualTo(10);
    }

    @Test
    @DisplayName("장르와 일치하는 만화 리스트를 한 페이지 가져옵니다 - 성공")
    void findAllByGenre200() {
        // given 1
        Author author = saveAuthorInRepository();
        CartoonSearchCond cartoonSearchDto = CartoonSearchCond.builder()
                .page(1)
                .genre("ROMANCE")
                .build();

        // given 2 - cartoonList
        List<Cartoon> cartoonGenreRomanceList = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .genre(Genre.ROMANCE)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonGenreAcitonList = IntStream.range(11, 16)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .genre(Genre.ACTION)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonGenreRomanceList);
        cartoonRepository.saveAll(cartoonGenreAcitonList);

        // when
        List<CartoonResponse> cartoonResponseList
                = cartoonService.findAllByCartoonCondOrderByLikes(cartoonSearchDto);

        // then
        assertThat(cartoonResponseList.size()).isEqualTo(10);
        assertThat(cartoonResponseList.get(0).getLikes()).isEqualTo(10);
    }

    @Test
    @DisplayName("만화 입력 조건 중 Enum 타입이 유효하면 메소드를 통과합니다")
    void validateSearchCond200() {
        // given
        CartoonSearchCond cartoonSearchCond = CartoonSearchCond.builder()
                .page(1)
                .dayOfTheWeek("MON")
                .genre("ROMANCE")
                .progress("SERIALIZATION")
                .build();

        // expected
        CartoonService.validateSearchCond(cartoonSearchCond);
    }

    @Test
    @DisplayName("만화 입력 조건 중 Enum 타입이 유효하지 않으면 예외가 발생합니다")
    void validateSearchCond400() {
        // given
        CartoonSearchCond cartoonSearchCond = CartoonSearchCond.builder()
                .page(1)
                .dayOfTheWeek("유효하지 않은 날짜")
                .genre("유효하지 않은 장르")
                .progress("유효하지 않은 진행상황")
                .build();

        // expected
        assertThrows(CartoonEnumTypeException.class,
                () -> CartoonService.validateSearchCond(cartoonSearchCond));
    }

    @Test
    @DisplayName("cartoonId와 일치하는 만화가 존재하면 수정에 성공합니다")
    void update200() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        Cartoon cartoon = saveCartoonInRepository(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        CartoonUpdateSet cartoonUpdateSet = CartoonUpdateSet.builder()
                .authorSession(authorSession)
                .cartoonId(cartoon.getId())
                .cartoonUpdate(cartoonUpdate)
                .build();

        // when
        CartoonResponse cartoonResponse = cartoonService.update(cartoonUpdateSet);

        // then
        assertThat(cartoonResponse.getTitle()).isEqualTo("수정 만화 제목");
        assertThat(cartoonResponse.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.TUE);
        assertThat(cartoonResponse.getProgress()).isEqualTo(Progress.COMPLETE);
        assertThat(cartoonResponse.getGenre()).isEqualTo(Genre.ROMANCE);
    }

    @Test
    @DisplayName("cartoonId와 일치하는 만화가 없다면 예외가 발생합니다")
    void update404() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        CartoonUpdateSet cartoonUpdateSet = CartoonUpdateSet.builder()
                .authorSession(authorSession)
                .cartoonId(9999L)
                .cartoonUpdate(cartoonUpdate)
                .build();

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.update(cartoonUpdateSet));
    }

    @Test
    @DisplayName("cartoonId와 일치하는 만화가 존재하면 만화를 삭제합니다")
    void delete200() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        Cartoon cartoon = saveCartoonInRepository(author);

        // when
        cartoonService.delete(authorSession, cartoon.getId());

        // then
        assertThat(cartoonRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 예외가 발생합니다")
    void delete404() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        saveCartoonInRepository(author);

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.delete(authorSession,9999L));
    }
}