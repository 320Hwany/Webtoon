package com.webtoon.author.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static com.webtoon.util.constant.ConstantCommon.BAD_REQUEST;
import static com.webtoon.util.constant.ConstantCommon.NOT_FOUND;
import static com.webtoon.util.constant.ConstantValid.*;
import static com.webtoon.util.enumerated.ErrorMessage.AUTHOR_NOT_FOUND;
import static com.webtoon.util.enumerated.ErrorMessage.VALID_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorControllerTest extends ControllerTest {

    @Test
    @DisplayName("작가 회원가입 - 성공")
    void signup200() throws Exception {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickname("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        String requestBody = objectMapper.writeValueAsString(authorSignup);

        // expected
        mockMvc.perform(post("/author/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(document("author/signup/200"));
    }

    @Test
    @DisplayName("조건에 맞지 않으면 작가 회원가입이 되지 않습니다- 실패")
    void signup400() throws Exception {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickname("")
                .email("abc")
                .password("")
                .build();

        String requestBody = objectMapper.writeValueAsString(authorSignup);

        // expected
        mockMvc.perform(post("/author/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
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
                .password("1234")
                .build();

        String requestBody = objectMapper.writeValueAsString(authorLogin);

        // expected
        mockMvc.perform(post("/author/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(author.getNickname()))
                .andExpect(jsonPath("$.email").value(author.getEmail()))
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

        String requestBody = objectMapper.writeValueAsString(authorLogin);

        // expected
        mockMvc.perform(post("/author/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.email").value(EMAIL_VALID_MESSAGE))
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

        String requestBody = objectMapper.writeValueAsString(authorLogin);

        // expected
        mockMvc.perform(post("/author/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(NOT_FOUND))
                .andExpect(jsonPath("$.message").value(AUTHOR_NOT_FOUND.getValue()))
                .andDo(document("author/login/404"));
    }

    @Test
    @DisplayName("작가 닉네임으로 작가 정보와 작가의 만화를 검색합니다 - 성공")
    void findAllByNickname200() throws Exception {
        // given
        Author author = saveAuthorInRepository();

        // expected
        mockMvc.perform(get("/author/nickname")
                        .param("page", "0")
                        .param("size", "20")
                        .param("nickname", "작가 이름"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.authorResponseList[0].nickname").value(author.getNickname()))
                .andExpect(jsonPath("$.authorResponseList[0].count").value(0))
                .andDo(document("author/nickname/200"));
    }

    @Test
    @DisplayName("검색 조건이 맞지 않으면 예외가 발생합니다 - 실패")
    void findAllByNickname400() throws Exception {
        // given
        saveAuthorInRepository();

        // expected
        mockMvc.perform(get("/author/nickname")
                        .param("page", "-1")
                        .param("size", "20")
                        .param("nickname", ""))
                .andExpect(status().isBadRequest())
                .andDo(document("author/nickname/400"));
    }

    @Test
    @DisplayName("로그인되어 있고 조건에 맞으면 회원정보가 수정됩니다 - 성공")
    void update200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickname("수정 닉네임")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        String requestBody = objectMapper.writeValueAsString(authorUpdate);

        // expected
        mockMvc.perform(patch("/author")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(document("author/update/200"));
    }

    @Test
    @DisplayName("로그인을 하더라도 조건에 맞지 않으면 정보를 수정할 수 없습니다 - 실패")
    void update400() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickname("")
                .email("abc")
                .password("")
                .build();

        String requestBody = objectMapper.writeValueAsString(authorUpdate);

        // expected
        mockMvc.perform(patch("/author")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(document("author/update/400"));
    }

    @Test
    @DisplayName("로그인되어 있지 않으면 회원 정보 수정을 할 수 없습니다 - 실패")
    void update401() throws Exception {
        // given
        saveAuthorInRepository();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickname("수정 닉네임")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        String requestBody = objectMapper.writeValueAsString(authorUpdate);

        // expected
        mockMvc.perform(patch("/author")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andDo(document("author/update/401"));
    }

    @Test
    @DisplayName("로그인 후 작가 계정을 삭제할 수 있습니다 - 성공")
    void delete200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

        // expected
        mockMvc.perform(delete("/author")
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
        mockMvc.perform(delete("/author")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(document("author/delete/401"));
    }

    @Test
    @DisplayName("로그아웃은 로그인을 한 후에 진행가능합니다 - 성공")
    void logout200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

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