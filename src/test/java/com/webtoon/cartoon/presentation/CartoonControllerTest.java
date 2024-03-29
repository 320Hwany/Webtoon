package com.webtoon.cartoon.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.util.ControllerTest;
import com.webtoon.util.constant.ConstantCommon;
import com.webtoon.util.constant.ConstantValid;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static com.webtoon.util.constant.ConstantCommon.*;
import static com.webtoon.util.constant.ConstantValid.CARTOON_TITLE_VALID_MESSAGE;
import static com.webtoon.util.constant.ConstantValid.PAGE_VALID_MESSAGE;
import static com.webtoon.util.enumerated.ErrorMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartoonControllerTest extends ControllerTest {

    @Test
    @DisplayName("작가로 로그인하면 만화를 등록할 수 있습니다 - 성공")
    void save201() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("만화 제목")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        String requestBody = objectMapper.writeValueAsString(cartoonSave);

        // expected
        mockMvc.perform(post("/cartoon")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(document("cartoon/save/201"));
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

        String requestBody = objectMapper.writeValueAsString(cartoonSave);

        // expected
        mockMvc.perform(post("/cartoon")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.message").value(ENUM_TYPE_VALIDATION.getValue()))
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

        String requestBody = objectMapper.writeValueAsString(cartoonSave);

        // expected
        mockMvc.perform(post("/cartoon")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(AUTHOR_UNAUTHORIZED.getValue()))
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
                        .param("page", "1")
                        .param("size", "20")
                        .param("title", "만화 제목"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(20))
                .andExpect(jsonPath("$.cartoonResponseList[0].title").value("만화 제목 30"))
                .andExpect(jsonPath("$.cartoonResponseList[0].likes").value(30))
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
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.page").value(PAGE_VALID_MESSAGE))
                .andDo(document("cartoon/get/title/400"));
    }

    @Test
    @DisplayName("입력한 요일에 맞는 만화 리스트를 가져옵니다")
    void getCartoonListByDay200() throws Exception {
        // given 1
        Author author = saveAuthorInRepository();

        // given 2 - cartoonList
        List<Cartoon> cartoonMONList = LongStream.range(1, 21)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("만화 제목 " + i)
                        .likes(i)
                        .dayOfTheWeek(DayOfTheWeek.MON)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonTUEList = LongStream.range(21, 30)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("만화 제목 " + i)
                        .likes(i)
                        .dayOfTheWeek(DayOfTheWeek.TUE)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonMONList);
        cartoonRepository.saveAll(cartoonTUEList);

        // expected
        mockMvc.perform(get("/cartoon/orderby/likes")
                        .param("page", "1")
                        .param("size", "20")
                        .param("dayOfTheWeek", "MON"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(20))
                .andExpect(jsonPath("$.cartoonResponseList[0].title").value("만화 제목 20"))
                .andExpect(jsonPath("$.cartoonResponseList[0].likes").value(20))
                .andDo(document("cartoon/get/day/200"));
    }

    @Test
    @DisplayName("입력한 장르의 만화 리스트를 보여줍니다 - 성공")
    void getCartoonListByGenre200() throws Exception {
        // given 1
        Author author = saveAuthorInRepository();

        // given 2 - cartoonList
        List<Cartoon> cartoonGenreRomanceList = IntStream.range(1, 21)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("만화 제목 " + i)
                        .genre(Genre.ROMANCE)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonGenreAcitonList = IntStream.range(21, 30)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .genre(Genre.ACTION)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonGenreRomanceList);
        cartoonRepository.saveAll(cartoonGenreAcitonList);

        // expected
        mockMvc.perform(get("/cartoon/orderby/likes")
                        .param("page", "1")
                        .param("genre", "ROMANCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(20))
                .andExpect(jsonPath("$.cartoonResponseList[0].title").value("만화 제목 20"))
                .andExpect(jsonPath("$.cartoonResponseList[0].likes").value(20))
                .andDo(document("cartoon/get/genre/200"));
    }

    @Test
    @DisplayName("입력한 만화 상황의 만화 리스트를 보여줍니다 - 성공")
    void getCartoonListByProgress200() throws Exception {
        // given 1
        Author author = saveAuthorInRepository();

        // given 2 - cartoonList
        List<Cartoon> cartoonProgressSerializationList = IntStream.range(1, 21)
                .mapToObj(i -> Cartoon.builder()
                        .author(author)
                        .title("만화 제목 " + i)
                        .progress(Progress.SERIALIZATION)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        List<Cartoon> cartoonProgressCompleteList = IntStream.range(21, 31)
                .mapToObj(i -> Cartoon.builder()
                        .title("만화 제목 " + i)
                        .progress(Progress.COMPLETE)
                        .likes(i)
                        .build())
                .collect(Collectors.toList());

        cartoonRepository.saveAll(cartoonProgressSerializationList);
        cartoonRepository.saveAll(cartoonProgressCompleteList);

        // expected
        mockMvc.perform(get("/cartoon/orderby/likes")
                        .param("page", "1")
                        .param("progress", "SERIALIZATION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(20))
                .andExpect(jsonPath("$.cartoonResponseList[0].title").value("만화 제목 20"))
                .andExpect(jsonPath("$.cartoonResponseList[0].likes").value(20))
                .andDo(document("cartoon/get/progress/200"));
    }

    @Test
    @DisplayName("입력사항을 잘못입력하면 오류메세지를 보여줍니다 - 실패")
    void getCartoonList400() throws Exception {
        // expected
        mockMvc.perform(get("/cartoon/orderby/likes")
                        .param("page", "1")
                        .param("genre", "존재하지 않는 장르"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(ENUM_TYPE_VALIDATION.getValue()))
                .andExpect(jsonPath("$.validation.Genre").value(GENRE_BAD_REQUEST.getValue()))
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

        String requestBody = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartoon.getId()))
                .andExpect(jsonPath("$.title").value("수정 만화 제목"))
                .andExpect(jsonPath("$.dayOfTheWeek").value(DayOfTheWeek.TUE.getValue()))
                .andExpect(jsonPath("$.progress").value(Progress.COMPLETE.getValue()))
                .andExpect(jsonPath("$.genre").value(Genre.ROMANCE.getValue()))
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

        String requestBody = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.title").value(CARTOON_TITLE_VALID_MESSAGE))
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

        String requestBody = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", cartoon.getId())
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.statusCode").value(UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(AUTHOR_UNAUTHORIZED.getValue()))
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

        String requestBody = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode").value(FORBIDDEN))
                .andExpect(jsonPath("$.message").value(CARTOON_FORBIDDEN.getValue()))
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

        String requestBody = objectMapper.writeValueAsString(cartoonUpdate);

        // expected
        mockMvc.perform(patch("/cartoon/{cartoonId}", 9999L)
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(NOT_FOUND))
                .andExpect(jsonPath("$.message").value(CARTOON_NOT_FOUND.getValue()))
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
                .andExpect(jsonPath("$.statusCode").value(UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(AUTHOR_UNAUTHORIZED.getValue()))
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
                .andExpect(jsonPath("$.statusCode").value(FORBIDDEN))
                .andExpect(jsonPath("$.message").value(CARTOON_FORBIDDEN.getValue()))
                .andDo(document("cartoon/delete/403"));
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 만화를 삭제할 수 없습니다 - 실패")
    void delete404() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);
        saveCartoonInRepository(author);

        // expected
        mockMvc.perform(delete("/cartoon/{cartoonId}", 9999L)
                        .session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(NOT_FOUND))
                .andExpect(jsonPath("$.message").value(CARTOON_NOT_FOUND.getValue()))
                .andDo(document("cartoon/delete/404"));
    }
}