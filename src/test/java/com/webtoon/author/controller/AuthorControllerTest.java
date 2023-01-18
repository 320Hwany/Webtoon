package com.webtoon.author.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class AuthorControllerTest extends ControllerTest {


    @Test
    @DisplayName("작가 회원가입 - 성공")
    void signup200() throws Exception {
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        String authorSignupJson = objectMapper.writeValueAsString(authorSignup);

        mockMvc.perform(post("/author/signup")
                        .contentType(APPLICATION_JSON)
                        .content(authorSignupJson))
                .andExpect(status().isOk())
                .andDo(document("author/signup/200"));
    }

    @Test
    @DisplayName("조건에 맞지 않으면 작가 회원가입이 되지 않습니다- 실패")
    void signup400() throws Exception {
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickName("")
                .email("abc")
                .password("")
                .build();
        String authorSignupJson = objectMapper.writeValueAsString(authorSignup);

        mockMvc.perform(post("/author/signup")
                        .contentType(APPLICATION_JSON)
                        .content(authorSignupJson))
                .andExpect(status().isBadRequest())
                .andDo(document("author/signup/400"));
    }

    @Test
    @DisplayName("작가 로그인 - 성공")
    void login200() throws Exception {
        // given
        Author author = saveAuthorInRepository();

        AuthorLogin authorLogin = AuthorLogin.builder()
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        String authorLoginJson = objectMapper.writeValueAsString(authorLogin);

        mockMvc.perform(post("/author/login")
                        .contentType(APPLICATION_JSON)
                        .content(authorLoginJson))
                .andExpect(status().isOk())
                .andDo(document("author/login/200"));
    }

    @Test
    @DisplayName("입력한 이메일, 비밀번호가 조건에 맞지 않으면 로그인이 되지 않습니다 - 실패")
    void login400() throws Exception {
        // given
        AuthorLogin authorLogin = AuthorLogin.builder()
                .email("조건에 맞지 않는 이메일")
                .password("")
                .build();

        String authorLoginJson = objectMapper.writeValueAsString(authorLogin);

        mockMvc.perform(post("/author/login")
                        .contentType(APPLICATION_JSON)
                        .content(authorLoginJson))
                .andExpect(status().isBadRequest())
                .andDo(document("author/login/400"));
    }

    @Test
    @DisplayName("작가 정보가 없으면 로그인이 되지 않습니다 - 실패")
    void login404() throws Exception {
        // given
        AuthorLogin authorLogin = AuthorLogin.builder()
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        String authorLoginJson = objectMapper.writeValueAsString(authorLogin);

        // expected
        mockMvc.perform(post("/author/login")
                        .contentType(APPLICATION_JSON)
                        .content(authorLoginJson))
                .andExpect(status().isNotFound())
                .andDo(document("author/login/404"));
    }

    @Test
    @DisplayName("작가 닉네임으로 작가를 검색합니다 - 성공")
    void getAuthorByNickName200() throws Exception {
        // given
        Author author = saveAuthorInRepository();

        // expected
        mockMvc.perform(get("/author/nickName")
                        .param("nickName", author.getNickName()))
                .andExpect(status().isOk())
                .andDo(document("author/get/nickname/200"));
    }

    @Test
    @DisplayName("검색한 닉네임의 작가가 존재하지 않으면 검색할 수 없습니다 - 실패")
    void getAuthorByNickName404() throws Exception {
        // expected
        mockMvc.perform(get("/author/nickName")
                        .param("nickName", "작가 이름"))
                .andExpect(status().isNotFound())
                .andDo(document("author/get/nickname/404"));
    }

    @Test
    @DisplayName("로그인되어 있고 조건에 맞으면 회원정보가 수정됩니다 - 성공")
    void update200() throws Exception {
        // given
        saveAuthorInRepository();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickName("수정 닉네임")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        String authorUpdateJson = objectMapper.writeValueAsString(authorUpdate);
        MockHttpSession session = loginAuthorSession();

        // expected
        mockMvc.perform(patch("/author")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(authorUpdateJson))
                .andExpect(status().isOk())
                .andDo(document("author/update/200"));
    }

    @Test
    @DisplayName("로그인을 하더라도 조건에 맞지 않으면 정보를 수정할 수 없습니다 - 실패")
    void update400() throws Exception {
        // given
        saveAuthorInRepository();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickName("")
                .email("abc")
                .password("")
                .build();

        String authorUpdateJson = objectMapper.writeValueAsString(authorUpdate);
        MockHttpSession session = loginAuthorSession();

        // expected
        mockMvc.perform(patch("/author")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(authorUpdateJson))
                .andExpect(status().isBadRequest())
                .andDo(document("author/update/400"));
    }

    @Test
    @DisplayName("로그인되어 있지 않으면 회원 정보 수정을 할 수 없습니다 - 실패")
    void update401() throws Exception {
        // given
        saveAuthorInRepository();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickName("수정 닉네임")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        String authorUpdateJson = objectMapper.writeValueAsString(authorUpdate);

        // expected
        mockMvc.perform(patch("/author")
                        .contentType(APPLICATION_JSON)
                        .content(authorUpdateJson))
                .andExpect(status().isUnauthorized())
                .andDo(document("author/update/401"));
    }

    @Test
    @DisplayName("로그인 후 작가 계정을 삭제할 수 있습니다 - 성공")
    void delete200() throws Exception {
        // given
        saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/author")
                        .session(session)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("author/delete/200"));
    }

    @Test
    @DisplayName("로그인을 하지 않으면 계정을 삭제할 수 없습니다 - 실패")
    void delete401() throws Exception {
        // given
        saveAuthorInRepository();

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/author")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(document("author/delete/401"));
    }

    @Test
    @DisplayName("로그아웃은 로그인을 한 후에 진행가능합니다 - 성공")
    void logout200() throws Exception {
        // given
        saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();

        // expected
        mockMvc.perform(post("/author/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("author/logout/200"));
    }

    @Test
    @DisplayName("로그인을 하지 않으면 로그아웃을 할 수 없습니다 - 실패")
    void logout401() throws Exception {
        // given
        saveAuthorInRepository();

        // expected
        mockMvc.perform(post("/author/logout"))
                .andExpect(status().isUnauthorized())
                .andDo(document("author/logout/401"));
    }
}