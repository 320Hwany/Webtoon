package com.webtoon.cartoon.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.cartoon.exception.EnumTypeValidException;
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
    @DisplayName("제목을 포함하는 만화가 있다면 한 페이지를 보여줍니다 - 성공")
    @Transactional
    void findAllByTitle200() {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .title("만화 제목")
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("NONE")
                .build();

        List<Cartoon> cartoonListKr = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonListEn = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .title("cartoon title " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonListKr);
        cartoonRepository.saveAll(cartoonListEn);

        // when
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> onePageCartoonList = cartoonService.findAllByTitle(cartoonSearch);

        // then
        assertThat(onePageCartoonList.size()).isEqualTo(10);
        assertThat(onePageCartoonList.get(0).getTitle()).isEqualTo("만화 제목 10");
    }


    @Test
    @DisplayName("장르와 일치하는 만화 리스트를 한 페이지 가져옵니다 - 성공")
    @Transactional
    void findAllByGenre200() {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("ROMANCE")
                .build();

        List<Cartoon> cartoonGenreRomanceList = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .genre(Genre.ROMANCE)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonGenreAcitonList = IntStream.range(11, 16)
                .mapToObj(i -> Cartoon.builder()
                        .genre(Genre.ACTION)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonGenreRomanceList);
        cartoonRepository.saveAll(cartoonGenreAcitonList);

        // when
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllByGenre(cartoonSearch);

        // then
        assertThat(cartoonList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("좋아요 수가 많은 순서로 만화 리스트를 한 페이지 가져옵니다 - 성공")
    @Transactional
    void findAllOrderByLikes200() {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("NONE")
                .build();

        List<Cartoon> cartoonList = LongStream.range(1, 31)
                .mapToObj(i -> Cartoon.builder()
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonList);

        // when
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> onePageCartoonList = cartoonService.findAllOrderByLikes(cartoonSearch);

        // then
        assertThat(onePageCartoonList.size()).isEqualTo(20);
        assertThat(onePageCartoonList.get(0).getLikes()).isEqualTo(30);
    }

    @Test
    @DisplayName("입력한 요일에 맞는 만화를 좋아요 많은 순으로 보여줍니다 - 성공")
    @Transactional
    void findAllByDayOfTheWeek() {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .dayOfTheWeek("MON")
                .progress("NONE")
                .genre("NONE")
                .build();

        List<Cartoon> cartoonMonList = LongStream.range(1, 21)
                .mapToObj(i -> Cartoon.builder()
                        .likes(i)
                        .dayOfTheWeek(DayOfTheWeek.MON)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonTueList = LongStream.range(21, 31)
                .mapToObj(i -> Cartoon.builder()
                        .likes(i)
                        .dayOfTheWeek(DayOfTheWeek.TUE)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonMonList);
        cartoonRepository.saveAll(cartoonTueList);

        // when
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> onePageCartoonList = cartoonService.findAllByDayOfTheWeek(cartoonSearch);

        // then
        assertThat(onePageCartoonList.size()).isEqualTo(20);
        assertThat(onePageCartoonList.get(0).getLikes()).isEqualTo(20);
    }

    @Test
    @DisplayName("작가가 만화에 대한 접근 권한이 있으면 메소드를 통과합니다 - 성공")
    void checkAuthorityForCartoon200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        // expected
        cartoonService.validateAuthorityForCartoon(authorSession, cartoon.getId());
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
                () -> cartoonService.validateAuthorityForCartoon(anotherAuthorSession, cartoon.getId()));
    }

    @Test
    @DisplayName("입력한 장르가 유효하면 메소드를 통과합니다 - 성공")
    void checkGenreValid200() {
        // expected
        cartoonService.validateGenreValid("ROMANCE");
    }

    @Test
    @DisplayName("입력한 장르가 유효하지 않으면 예외가 발생합니다 - 실패")
    void checkGenreValid400() {
        // expected
        assertThrows(EnumTypeValidException.class,
                () -> cartoonService.validateGenreValid("존재하지 않는 장르"));
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