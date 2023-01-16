package com.webtoon.cartoon.controller;

import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

@Transactional
class CartoonControllerTest extends ControllerTest {


    @Test
    @DisplayName("작가로 로그인하면 만화를 등록할 수 있습니다 - 성공")
    void save() throws Exception {
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("cartoon/save"));
    }

    @Test
    @DisplayName("작가로 로그인하지 않으면 만화를 등록할 수 없습니다 - 실패")
    void saveFailByUnauthorized() throws Exception {
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
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(document("cartoon/saveFailByUnauthorized"));
    }

    @Test
    @DisplayName("작가로 로그인해도 조건에 맞지 않으면 만화를 등록할 수 없습니다 - 실패")
    void saveFailByValid() throws Exception {
        // given
        saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();

        CartoonSave cartoonSave = CartoonSave.builder()
                .title("")
                .dayOfTheWeek("")
                .progress("")
                .build();

        String cartoonSaveJson = objectMapper.writeValueAsString(cartoonSave);

        // expected
        mockMvc.perform(post("/cartoon")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(cartoonSaveJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(document("cartoon/saveFailByValid"));
    }
}