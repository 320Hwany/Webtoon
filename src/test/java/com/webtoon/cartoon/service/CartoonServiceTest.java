package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.cartoon.repository.CartoonJpaRepository;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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
        assertThat(cartoonRepository.count()).isEqualTo(1L);
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
    @DisplayName("제목을 포함하는 만화가 있다면 검색 결과를 보여줍니다 - 성공")
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
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonListEn = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .title("cartoon title " + i)
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
                        .title("만화 제목 " + i)
                        .genre(Genre.ROMANCE)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonGenreAcitonList = IntStream.range(11, 16)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
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
                        .title("만화 제목 " + i)
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
        cartoonService.update(cartoon.getId(), cartoonUpdate);

        // then
        assertThat(cartoon.getTitle()).isEqualTo("수정 만화 제목");
        assertThat(cartoon.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.TUE);
        assertThat(cartoon.getProgress()).isEqualTo(Progress.COMPLETE);
        assertThat(cartoon.getGenre()).isEqualTo(Genre.ROMANCE);
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

        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.update(9999L,  cartoonUpdate));
    }

    @Test
    @DisplayName("만화가 존재하면 만화를 삭제합니다 - 성공")
    void delete200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        // when
        cartoonService.delete(cartoon.getId());

        // then
        assertThat(cartoonRepository.count()).isEqualTo(0L);
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 만화를 예외가 발생합니다 - 실패")
    void delete404() {
        // expected
        assertThrows(CartoonNotFoundException.class,
                () -> cartoonService.delete(1L));
    }


    @Test
    @DisplayName("작가가 만화에 대한 접근 권한이 있는면 메소드를 통과합니다 - 성공")
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
                .nickName("다른 회원")
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
}