package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class CartoonServiceTest extends ServiceTest {

    @Autowired
    private CartoonService cartoonService;

    @Test
    @DisplayName("AuthorSession과 id값이 일치하는 Author로 만화를 등록합니다 - 성공")
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
        Cartoon cartoon = cartoonService.save(cartoonSave, authorSession);

        // then
        assertThat(cartoon.getTitle()).isEqualTo("만화 제목");
        assertThat(cartoon.getAuthor()).isEqualTo(author);
        assertThat(cartoon.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.MON);
        assertThat(cartoon.getProgress()).isEqualTo(Progress.SERIALIZATION);
        assertThat(cartoon.getGenre()).isEqualTo(Genre.ROMANCE);
    }

    @Test
    @DisplayName("AuthorSession과 id값이 일치하는 Author가 없다면 만화 등록을 할 수 없습니다 - 실패")
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
                .nickName("DB에 없는 회원")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        // expected
        assertThrows(AuthorNotFoundException.class,
                () -> cartoonService.save(cartoonSave, authorSession));
    }

    @Test
    @DisplayName("제목이 일치하는 만화가 있다면 검색 결과를 보여줍니다 - 성공")
    @Transactional
    void getByTitle200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        // when
        Cartoon findCartoon = cartoonService.getByTitle(cartoon.getTitle());

        // then
        assertThat(findCartoon).isEqualTo(cartoon);
    }

    @Test
    @DisplayName("제목이 일치하는 만화가 없다면 검색 결과를 보여줄 수 없습니다 - 실패")
    @Transactional
    void getByTitle404() {
        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.getByTitle("없는 제목"));
    }

    @Test
    @DisplayName("장르와 일치하는 만화 리스트를 가져옵니다 - 성공")
    @Transactional
    void findAllByGenre200() {
        // given
        Author author = saveAuthorInRepository();
        saveCartoonInRepository(author);
        Cartoon anotherCartoon = Cartoon.builder()
                .genre(Genre.ACTION)
                .build();

        cartoonRepository.save(anotherCartoon);
        Genre genre = Genre.valueOf("ROMANCE");
        List<Genre> genreList = new ArrayList<>();

        // when
        List<Cartoon> cartoonList = cartoonService.findAllByGenre(genre);

        for (Cartoon cartoon : cartoonList) {
            genreList.add(cartoon.getGenre());
        }

        // then
        assertThat(genreList.size()).isEqualTo(1);
        assertThat(genreList).contains(Genre.ROMANCE);
        assertThat(genreList).doesNotContain(Genre.ACTION);
    }


    @Test
    @DisplayName("작가가 만화에 대한 접근 권한이 있는면 메소드를 통과합니다 - 성공")
    void checkAuthorityForCartoon200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        // expected
        cartoonService.checkAuthorityForCartoon(cartoon.getId(), authorSession);
    }

    @Test
    @DisplayName("작가가 만화에 대한 접근 권한이 없으면 예외가 발생합니다 - 실패")
    void checkAuthorityForCartoon403() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        AuthorSession anotherAuthorSession = AuthorSession.builder()
                .id(9999L)
                .nickName("다른 회원")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        // expected
        assertThrows(CartoonForbiddenException.class,
                () -> cartoonService.checkAuthorityForCartoon(cartoon.getId(), anotherAuthorSession));
    }

    @Test
    @DisplayName("CartoonEnumField의 정보가 Enum에 존재하면 메소드를 통과합니다 - 성공")
    void checkEnumTypeValid200() {
        // given
        CartoonEnumField cartoonEnumField = CartoonEnumField.builder()
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        // expected
        cartoonService.checkEnumTypeValid(cartoonEnumField);
    }

    @Test
    @DisplayName("CartoonEnumField의 정보가 Enum에 존재하지 않으면 예외가 발생합니다 - 실패")
    void checkEnumTypeValid400() {
        // given
        CartoonEnumField cartoonEnumField = CartoonEnumField.builder()
                .dayOfTheWeek("존재하지 않는 요일")
                .progress("존재하지 않는 진행과정")
                .genre("존재하지 않는 장르")
                .build();

        // expected
        assertThrows(EnumTypeValidException.class,
                () -> cartoonService.checkEnumTypeValid(cartoonEnumField));
    }

    @Test
    @DisplayName("장르 String과 일치하는 장르가 있다면 Genre변수로 가져옵니다 - 성공")
    void getGenreFromString200() {
        // given
        String genreString = "ROMANCE";

        // when
        Genre genre = cartoonService.getGenreFromString(genreString);

        // then
        assertThat(genre).isInstanceOf(Genre.class);
    }

    @Test
    @DisplayName("장르 String과 일치하는 장르가 없다면 예외가 발생합니다 - 실패")
    void getGenreFromString400() {
        // given
        String genreString = "존재하지 않는 장르";

        // expected
        assertThrows(EnumTypeValidException.class,
                () -> cartoonService.getGenreFromString(genreString));
    }

    @Test
    @DisplayName("만화가 존재하면 수정에 성공합니다")
    @Transactional
    void update200() {
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
        Cartoon afterUpdate = cartoonService.update(cartoon.getId(), cartoonUpdate);

        // then
        assertThat(afterUpdate).isEqualTo(cartoon);
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 수정할 수 없습니다 - 실패")
    void update404() {
        // given
        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        // when
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.update(9999L,  cartoonUpdate));
    }
}