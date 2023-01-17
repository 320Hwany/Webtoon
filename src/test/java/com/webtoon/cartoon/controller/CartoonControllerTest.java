package com.webtoon.cartoon.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class CartoonControllerTest extends ControllerTest {

    @BeforeEach
    void clean() {
        cartoonRepository.deleteAll();
        authorRepository.deleteAll();
    }

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
    @DisplayName("제목이 일치하는 만화가 있다면 검색 결과를 보여줍니다 - 성공")
    void getCartoonByTitle200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        // expected
        mockMvc.perform(get("/cartoon/title")
                        .param("title", cartoon.getTitle()))
                .andExpect(status().isOk())
                .andDo(document("cartoon/get/title/200"));
    }

    @Test
    @DisplayName("제목이 일치하는 만화가 없다면 검색 결과를 보여줄 수 없습니다 - 실패")
    void getCartoonByTitle404() throws Exception {
        // expected
        mockMvc.perform(get("/cartoon/title")
                        .param("title", "없는 제목"))
                .andExpect(status().isNotFound())
                .andDo(document("cartoon/get/title/404"));
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