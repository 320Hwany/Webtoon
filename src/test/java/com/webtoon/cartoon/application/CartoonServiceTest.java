package com.webtoon.cartoon.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonSearchTitle;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartoonServiceTest extends ServiceTest {

    @Autowired
    private CartoonService cartoonService;

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
        cartoonService.save(cartoonSave, authorSession);

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
                () -> cartoonService.save(cartoonSave, authorSession));
    }

    @Test
    @DisplayName("제목을 포함하는 만화가 있다면 한 페이지를 보여줍니다 - 성공")
    void findAllByTitle200() {
        // given
        Author author = saveAuthorInRepository();

        CartoonSearchTitle cartoonSearchTitle = CartoonSearchTitle.builder()
                .page(0)
                .size(20)
                .title("만화 제목")
                .build();

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
        List<CartoonResponse> cartoonResponseList = cartoonService.findAllByTitleSet(cartoonSearchTitle);

        // then
        assertThat(cartoonResponseList.size()).isEqualTo(10);
        assertThat(cartoonResponseList.get(0).getTitle()).isEqualTo("만화 제목 10");
    }

    @Test
    @DisplayName("입력한 요일에 맞는 만화를 좋아요 많은 순으로 보여줍니다 - 성공")
    void findAllByDayOfTheWeek() {
        // given
        Author author = saveAuthorInRepository();
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .dayOfTheWeek("MON")
                .progress("NONE")
                .build();

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
                = cartoonService.findAllByCartoonCondOrderByLikesSet(cartoonSearchDto);

        // then
        assertThat(cartoonResponseList.size()).isEqualTo(20);
        assertThat(cartoonResponseList.get(0).getLikes()).isEqualTo(20);
    }

    @DisplayName("만화 상태와 일치하는 만화 리스트를 한 페이지 가져옵니다 - 성공")
    @Transactional
    void findAllByProgress200() {
        // given
        Author author = saveAuthorInRepository();
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .dayOfTheWeek("NONE")
                .progress("SERIALIZATION")
                .genre("NONE")
                .build();

        List<Cartoon> cartoonProgressSerializationList = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .progress(Progress.SERIALIZATION)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonProgressCompleteList = IntStream.range(11, 16)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .progress(Progress.COMPLETE)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonProgressSerializationList);
        cartoonRepository.saveAll(cartoonProgressCompleteList);

        // when
        List<CartoonResponse> cartoonResponseList
                = cartoonService.findAllByCartoonCondOrderByLikesSet(cartoonSearchDto);

        // then
        assertThat(cartoonResponseList.size()).isEqualTo(10);
    }
    @Test
    @DisplayName("장르와 일치하는 만화 리스트를 한 페이지 가져옵니다 - 성공")
    @Transactional
    void findAllByGenre200() {
        // given
        Author author = saveAuthorInRepository();
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("ROMANCE")
                .build();

        List<Cartoon> cartoonGenreRomanceList = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .genre(Genre.ROMANCE)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonGenreAcitonList = IntStream.range(11, 16)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .genre(Genre.ACTION)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonGenreRomanceList);
        cartoonRepository.saveAll(cartoonGenreAcitonList);

        // when
        List<CartoonResponse> cartoonResponseList
                = cartoonService.findAllByCartoonCondOrderByLikesSet(cartoonSearchDto);

        // then
        assertThat(cartoonResponseList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("작가가 만화에 대한 접근 권한이 있으면 메소드를 통과합니다 - 성공")
    void checkAuthorityForCartoon200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        // expected
        cartoonService.validateAuthorityForCartoon(authorSession, cartoon);
    }

    @Test
    @DisplayName("작가가 만화에 대한 접근 권한이 없으면 예외가 발생합니다 - 실패")
    void checkAuthorityForCartoon403() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        AuthorSession anotherAuthorSession = AuthorSession.builder()
                .id(9999L)
                .nickname("다른 회원")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        // expected
        assertThrows(CartoonForbiddenException.class,
                () -> cartoonService.validateAuthorityForCartoon(anotherAuthorSession, cartoon));
    }

    @Test
    @DisplayName("cartoonId와 일치하는 만화가 존재하면 수정에 성공합니다")
    @Transactional
    void updateSet200() {
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
        // when
        cartoonService.update(authorSession, cartoonUpdate, cartoon.getId());

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
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        Cartoon cartoon = saveCartoonInRepository(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.update(authorSession, cartoonUpdate,9999L));
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
        Cartoon cartoon = saveCartoonInRepository(author);

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.delete(authorSession,9999L));
    }


    @Test
    @DisplayName("좋아요를 누르면 Cartoon의 좋아요 개수가 1 증가합니다")
    void addLike() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        // when
        cartoonService.addLike(cartoon);

        // then
        assertThat(cartoon.getLikes()).isEqualTo(1);
    }
}