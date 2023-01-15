package com.webtoon.author.controller;

import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorControllerTest extends ControllerTest {

    @Test
    @DisplayName("작가 회원가입 - 성공")
    void signupSuccess() throws Exception {
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(authorSignup);

        mockMvc.perform(post("/author/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("author/signup"));
    }

    @Test
    @DisplayName("조건에 맞지 않으면 작가 회원가입이 되지 않습니다- 실패")
    void signupFail() throws Exception {
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName("")
                .email("abc")
                .password("")
                .build();
        String json = objectMapper.writeValueAsString(authorSignup);

        mockMvc.perform(post("/author/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(document("author/signupFail"));
    }

//    @Test
//    @DisplayName("작가 로그인 - 성공")
//    void loginSuccess() throws Exception {
//        AuthorLogin authorLogin = AuthorLogin.builder()
//                .email("yhwjd99@gmail.com")
//                .password("1234")
//                .build();
//
//        AuthorSession authorSession = AuthorSession.builder()
//                .id(1L)
//                .nickName("닉네임")
//                .email("yhwjd99@gmail.com")
//                .password("1234")
//                .build();
//
//        String json = objectMapper.writeValueAsString(authorLogin);
//        given(authorService.makeAuthorSession(authorLogin))
//                .willReturn(authorSession);
//
//        mockMvc.perform(post("/author/login")
//                        .contentType(APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk());
//    }
}