package com.webtoon.cartoon.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.util.ControllerTest;
import com.webtoon.util.enumerated.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class CartoonControllerTest extends ControllerTest {


    @Test
    @DisplayName("작가로 로그인하면 만화를 등록할 수 있습니다 - 성공")
    void save200() throws Exception {
        // given
        saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        String cartoonSaveJson = objectMapper.writeValueAsString(cartoonSave);

        // expected
        mockMvc.perform(post("/cartoon")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSaveJson))
                .andExpect(status().isOk())
                .andDo(document("cartoon/save/200"));
    }

    @Test
    @DisplayName("작가로 로그인해도 조건에 맞지 않으면 만화를 등록할 수 없습니다 - 실패")
    void save400() throws Exception {
        // given
        saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("")
                .progress("")
                .genre("ROMANCE")
                .build();

        String cartoonSaveJson = objectMapper.writeValueAsString(cartoonSave);

        // expected
        mockMvc.perform(post("/cartoon")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSaveJson))
                .andExpect(status().isBadRequest())
                .andDo(document("cartoon/save/400"));
    }

    @Test
    @DisplayName("작가로 로그인하지 않으면 만화를 등록할 수 없습니다 - 실패")
    void save401() throws Exception {
        // given
        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        String cartoonSaveJson = objectMapper.writeValueAsString(cartoonSave);

        // expected
        mockMvc.perform(post("/cartoon")
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSaveJson))
                .andExpect(status().isUnauthorized())
                .andDo(document("cartoon/save/401"));
    }

    @Test
    @DisplayName("입력한 제목을 포함하는 만화 리스트를 한 페이지 보여줍니다 - 성공")
    void getCartoonListByTitle200() throws Exception {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(1)
                .title("만화 제목")
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("NONE")
                .build();

        List<Cartoon> cartoonList = IntStream.range(1, 31)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonList);

        String cartoonSearchDtoJson = objectMapper.writeValueAsString(cartoonSearchDto);

        // expected
        mockMvc.perform(get("/cartoon/title")
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSearchDtoJson))
                .andExpect(status().isOk())
                .andDo(document("cartoon/get/title/200"));
    }

    @Test
    @DisplayName("페이지를 잘못입력하면 검색 결과를 보여줄 수 없습니다 - 실패")
    void getCartoonListByTitle404() throws Exception {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(-10)
                .title("만화 제목")
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("NONE")
                .build();

        List<Cartoon> cartoonList = IntStream.range(1, 31)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonList);

        String cartoonSearchDtoJson = objectMapper.writeValueAsString(cartoonSearchDto);

        // expected
        mockMvc.perform(get("/cartoon/title")
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSearchDtoJson))
                .andExpect(status().isBadRequest())
                .andDo(document("cartoon/get/title/404"));
    }

    @Test
    @DisplayName("입력한 장르의 만화 리스트를 보여줍니다 - 성공")
    void getCartoonListByGenre200() throws Exception {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(1)
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

        List<Cartoon> cartoonGenreAcitonList = IntStream.range(11, 21)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .genre(Genre.ACTION)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonGenreRomanceList);
        cartoonRepository.saveAll(cartoonGenreAcitonList);

        String cartoonSearchDtoJson = objectMapper.writeValueAsString(cartoonSearchDto);

        // expected
        mockMvc.perform(get("/cartoon/genre")
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSearchDtoJson))
                .andExpect(status().isOk())
                .andDo(document("cartoon/get/genre/200"));
    }

    @Test
    @DisplayName("페이지를 잘못 입력하거나 장르를 잘못입력하면 오류메세지를 보여줍니다 - 실패")
    void getCartoonListByGenre400() throws Exception {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(-10)
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("존재하지 않는 장르")
                .build();

        String cartoonSearchDtoJson = objectMapper.writeValueAsString(cartoonSearchDto);

        // expected
        mockMvc.perform(get("/cartoon/genre")
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSearchDtoJson))
                .andExpect(status().isBadRequest())
                .andDo(document("cartoon/get/genre/400"));
    }

    @Test
    @DisplayName("좋아요가 많은 순으로 만화 리스트를 한 페이지 보여줍니다 - 성공")
    void getCartoonListOrderByLikes200() throws Exception {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(1)
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("NONE")
                .build();

        List<Cartoon> cartoonList = IntStream.range(1, 31)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonList);

        String cartoonSearchDtoJson = objectMapper.writeValueAsString(cartoonSearchDto);

        // expected
        mockMvc.perform(get("/cartoon/likes")
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSearchDtoJson))
                .andExpect(status().isOk())
                .andDo(document("cartoon/get/likes/200"));
    }

    @Test
    @DisplayName("페이지를 잘못 입력하면 좋아요가 많은 순으로 페이지를 가져올 수 없습니다 - 실패")
    void getCartoonListOrderByLikes400() throws Exception {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(-10)
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("NONE")
                .build();

        List<Cartoon> cartoonList = IntStream.range(1, 31)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonList);

        String cartoonSearchDtoJson = objectMapper.writeValueAsString(cartoonSearchDto);

        // expected
        mockMvc.perform(get("/cartoon/likes")
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSearchDtoJson))
                .andExpect(status().isBadRequest())
                .andDo(document("cartoon/get/likes/400"));
    }

    @Test
    @DisplayName("작가로 로그인하면 만화를 수정할 수 있습니다 - 성공")
    void update200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();
        Cartoon cartoon = saveCartoonInRepository(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        String cartoonUpdateJson = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(cartoonUpdateJson))
                .andExpect(status().isOk())
                .andDo(document("cartoon/update/200"));
    }

    @Test
    @DisplayName("작가로 로그인해도 조건에 맞지 않으면 만화를 수정할 수 없습니다 - 실패")
    void update400() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();
        Cartoon cartoon = saveCartoonInRepository(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("")
                .dayOfTheWeek("TUE")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        String cartoonUpdateJson = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(cartoonUpdateJson))
                .andExpect(status().isBadRequest())
                .andDo(document("cartoon/update/400"));
    }

    @Test
    @DisplayName("작가로 로그인하지 않으면 만화를 수정할 수 없습니다 - 실패")
    void update401() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        String cartoonUpdateJson = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", cartoon.getId())
                        .contentType(APPLICATION_JSON)
                        .content(cartoonUpdateJson))
                .andExpect(status().isUnauthorized())
                .andDo(document("cartoon/update/401"));
    }

    @Test
    @DisplayName("작가로 로그인해도 해당 작가의 만화가 아니면 수정할 수 없습니다 - 실패")
    void update403() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();

        Author anotherAuthor = Author.builder()
                .nickName("다른 작가 이름")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();
        Cartoon cartoon = saveCartoonInRepository(anotherAuthor);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        authorRepository.save(anotherAuthor);
        String cartoonUpdateJson = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(cartoonUpdateJson))
                .andExpect(status().isForbidden())
                .andDo(document("cartoon/update/403"));
    }
}