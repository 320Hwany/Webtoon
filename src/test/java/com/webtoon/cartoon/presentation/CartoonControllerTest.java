package com.webtoon.cartoon.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.util.ControllerTest;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartoonControllerTest extends ControllerTest {

    @Test
    @DisplayName("작가로 로그인하면 만화를 등록할 수 있습니다 - 성공")
    void save200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

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
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

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
        Author author = saveAuthorInRepository();

        List<Cartoon> cartoonList = LongStream.range(1, 31)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonList);

        // expected
        mockMvc.perform(get("/cartoon/title")
                        .param("page", "0")
                        .param("size", "20")
                        .param("title", "만화 제목"))
                .andExpect(status().isOk())
                .andDo(document("cartoon/get/title/200"));
    }

    @Test
    @DisplayName("페이지를 잘못입력하면 검색 결과를 보여줄 수 없습니다 - 실패")
    void getCartoonListByTitle400() throws Exception {
        // given
        List<Cartoon> cartoonList = LongStream.range(1, 31)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonList);

        // expected
        mockMvc.perform(get("/cartoon/title")
                        .param("page", "-10")
                        .param("size", "20")
                        .param("title", "만화 제목"))
                .andExpect(status().isBadRequest())
                .andDo(document("cartoon/get/title/400"));
    }

    @Test
    @DisplayName("입력한 요일에 맞는 만화 리스트를 가져옵니다")
    void getCartoonListByDay200() throws Exception {
        // given
        Author author = saveAuthorInRepository();

        List<Cartoon> cartoonList = LongStream.range(1, 31)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("만화 제목 " + i)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonList);

        // expected
        mockMvc.perform(get("/cartoon/orderby/likes")
                        .param("page", "0")
                        .param("size", "20")
                        .param("dayOfTheWeek", "MON")
                        .param("progress", "NONE")
                        .param("genre", "NONE"))
                .andExpect(status().isOk())
                .andDo(document("cartoon/get/day/200"));
    }

    @Test
    @DisplayName("입력한 장르의 만화 리스트를 보여줍니다 - 성공")
    void getCartoonListByGenre200() throws Exception {
        // given
        Author author = saveAuthorInRepository();

        List<Cartoon> cartoonGenreRomanceList = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
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

        // expected
        mockMvc.perform(get("/cartoon/orderby/likes")
                        .param("page", "1")
                        .param("genre", "ROMANCE"))
                .andExpect(status().isOk())
                .andDo(document("cartoon/get/genre/200"));
    }

    @Test
    @DisplayName("입력한 만화 상황의 만화 리스트를 보여줍니다 - 성공")
    void getCartoonListByProgress200() throws Exception {
        // given
        Author author = saveAuthorInRepository();

        List<Cartoon> cartoonProgressSerializationList = IntStream.range(1, 11)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("만화 제목 " + i)
                        .progress(Progress.SERIALIZATION)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonProgressCompleteList = IntStream.range(11, 21)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .progress(Progress.COMPLETE)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonProgressSerializationList);
        cartoonRepository.saveAll(cartoonProgressCompleteList);

        // expected
        mockMvc.perform(get("/cartoon/orderby/likes")
                        .param("page", "1")
                        .param("progress", "SERIALIZATION"))
                .andExpect(status().isOk())
                .andDo(document("cartoon/get/progress/200"));
    }

    @Test
    @DisplayName("입력사항을 잘못입력하면 오류메세지를 보여줍니다 - 실패")
    void getCartoonList400() throws Exception {
        // expected
        mockMvc.perform(get("/cartoon/orderby/likes")
                        .param("genre", "존재하지 않는 장르"))
                .andExpect(status().isBadRequest())
                .andDo(document("cartoon/get/genre/400"));
    }

    @Test
    @DisplayName("작가로 로그인하면 만화를 수정할 수 있습니다 - 성공")
    void update200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);
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
        MockHttpSession session = loginAuthorSession(author);
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
        MockHttpSession session = loginAuthorSession(author);

        Author anotherAuthor = Author.builder()
                .nickname("다른 작가 이름")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        authorRepository.save(anotherAuthor);
        Cartoon cartoon = saveCartoonInRepository(anotherAuthor);

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
                .andExpect(status().isForbidden())
                .andDo(document("cartoon/update/403"));
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 만화를 수정할 수 없습니다 - 실패")
    void update404() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);
        saveCartoonInRepository(author);

        CartoonUpdate cartoonUpdate = CartoonUpdate.builder()
                .title("수정 만화 제목")
                .dayOfTheWeek("TUE")
                .progress("COMPLETE")
                .genre("ROMANCE")
                .build();

        String cartoonUpdateJson = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", 9999L)
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(cartoonUpdateJson))
                .andExpect(status().isNotFound())
                .andDo(document("cartoon/update/404"));
    }

    @Test
    @DisplayName("작가로 로그인하면 자기 만화를 삭제할 수 있습니다 - 성공")
    void delete200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);
        Cartoon cartoon = saveCartoonInRepository(author);

        // expected
        mockMvc.perform(delete("/cartoon/{cartoonId}", cartoon.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoon/delete/200"));
    }

    @Test
    @DisplayName("작가로 로그인을 하지 않으면 만화를 삭제할 수 없습니다 - 실패")
    void delete401() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        // expected
        mockMvc.perform(delete("/cartoon/{cartoonId}", cartoon.getId()))
                .andExpect(status().isUnauthorized())
                .andDo(document("cartoon/delete/401"));
    }

    @Test
    @DisplayName("작가로 로그인하여도 자기 만화가 아니면 삭제할 수 없습니다 - 실패")
    void delete403() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

        Author anotherAuthor = Author.builder()
                .nickname("다른 작가 이름")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        authorRepository.save(anotherAuthor);
        Cartoon anotherCartoon = saveCartoonInRepository(anotherAuthor);

        // expected
        mockMvc.perform(delete("/cartoon/{cartoonId}", anotherCartoon.getId())
                        .session(session))
                .andExpect(status().isForbidden())
                .andDo(document("cartoon/delete/403"));
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 만화를 삭제할 수 없습니다 - 실패")
    void delete404() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);
        Cartoon cartoon = saveCartoonInRepository(author);

        // expected
        mockMvc.perform(delete("/cartoon/{cartoonId}", 9999L)
                        .session(session))
                .andExpect(status().isNotFound())
                .andDo(document("cartoon/delete/404"));
    }
}