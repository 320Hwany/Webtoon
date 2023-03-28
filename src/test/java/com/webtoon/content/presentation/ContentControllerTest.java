package com.webtoon.content.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.time.LocalDate;

import static com.webtoon.util.constant.ConstantCommon.*;
import static com.webtoon.util.constant.ConstantValid.*;
import static com.webtoon.util.enumerated.ErrorMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContentControllerTest extends ControllerTest {

    @Test
    @DisplayName("작가로 로그인한 후 자신의 만화에 내용을 추가할 수 있습니다 - 성공")
    void save201() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        MockHttpSession session = loginAuthorSession(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        String requestBody = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(document("content/save/201"));
    }

    @Test
    @DisplayName("ContentSave가 조건에 맞지 않으면 예외가 발생합니다 - 실패")
    void save400() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        MockHttpSession session = loginAuthorSession(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("")
                .episode(-1)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        String requestBody = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.subTitle").value(SUBTITLE_VALID_MESSAGE))
                .andExpect(jsonPath("$.validation.episode").value(EPISODE_VALID_MESSAGE))
                .andDo(document("content/save/400"));
    }

    @Test
    @DisplayName("로그인을 하지 않으면 만화 내용을 추가할 수 없습니다 - 실패")
    void save401() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        String requestBody = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", cartoon.getId())
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(AUTHOR_UNAUTHORIZED.getValue()))
                .andDo(document("content/save/401"));
    }

    @Test
    @DisplayName("로그인을 하더라도 작가의 만화가 아니면 접근 권한 예외가 발생합니다 - 실패")
    void save403() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

        Author anotherAuthor = Author.builder()
                .nickname("다른 작가 이름")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        authorRepository.save(anotherAuthor);
        Cartoon cartoon = saveCartoonInRepository(anotherAuthor);
        String requestBody = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode").value(FORBIDDEN))
                .andExpect(jsonPath("$.message").value(CARTOON_FORBIDDEN.getValue()))
                .andDo(document("content/save/403"));
    }

    @Test
    @DisplayName("로그인을 하더라도 만화를 찾을 수 없으면 만화 내용을 등록할 수 없습니다 - 실패")
    void save404() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        String contentSaveJson = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", 9999L)
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(contentSaveJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(NOT_FOUND))
                .andExpect(jsonPath("$.message").value(CARTOON_NOT_FOUND.getValue()))
                .andDo(document("content/save/404"));
    }

    @Test
    @DisplayName("만화의 에피소드가 존재하면 컨텐츠를 가져옵니다 - 성공")
    void getContent200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(get("/content")
                        .param("page", "1")
                        .param("size", "20")
                        .param("cartoonId", String.valueOf(cartoon.getId()))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.cartoonResponseList[0].episode").value(content.getEpisode()))
                .andDo(document("content/get/200"));
    }

    @Test
    @DisplayName("입력한 정보가 조건에 맞지 않으면 예외가 발생합니다 - 실패")
    void getContent400() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(get("/content")
                        .param("page", "-1")
                        .param("size", "20")
                        .param("cartoonId", "9999")
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andDo(document("content/get/400"));
    }

    @Test
    @DisplayName("만화가 존재하지 않으면 예외를 보여줍니다 - 실패")
    void getContent404Cartoon() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(get("/content")
                        .param("page", "1")
                        .param("size", "20")
                        .param("cartoonId", "9999")
                        .session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(NOT_FOUND))
                .andExpect(jsonPath("$.message").value(CARTOON_NOT_FOUND.getValue()))
                .andDo(document("content/get/404Cartoon"));
    }

    @Test
    @DisplayName("만화의 에피소드를 가져옵니다")
    void getEpisode200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(get("/content/episode")
                        .param("page", "1")
                        .param("size", "20")
                        .param("cartoonId", String.valueOf(cartoon.getId()))
                        .param("contentEpisode", String.valueOf(content.getEpisode()))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.episode").value(content.getEpisode()))
                .andDo(document("content/episode/get/200"));
    }

    @Test
    @DisplayName("입력한 정보가 조건에 맞지 않으면 예외가 발생합니다")
    void getEpisode400() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(get("/content/episode")
                        .param("page", "-10")
                        .param("size", "20")
                        .param("cartoonId", String.valueOf(cartoon.getId()))
                        .param("contentEpisode", String.valueOf(content.getEpisode()))
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andDo(document("content/episode/get/400"));
    }

    @Test
    @DisplayName("입력한 수정 정보로 수정합니다 - 성공")
    void update200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        MockHttpSession session = loginAuthorSession(author);

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .registrationDate(LocalDate.of(1999, 3, 20))
                .build();

        String requestBody = objectMapper.writeValueAsString(contentUpdate);

        // expected
        mockMvc.perform(patch("/content/{cartoonId}/{contentEpisode}",
                        cartoon.getId(), content.getEpisode())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subTitle").value(contentUpdate.getSubTitle()))
                .andExpect(jsonPath("$.episode").value(contentUpdate.getEpisode()))
                .andExpect(jsonPath("$.registrationDate")
                        .value(String.valueOf(contentUpdate.getRegistrationDate())))
                .andDo(document("content/update/200"));
    }

    @Test
    @DisplayName("입력 내용이 조건에 맞지 않으면 수정에 실패합니다 - 실패")
    void update400() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        MockHttpSession session = loginAuthorSession(author);

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("")
                .episode(-10)
                .registrationDate(null)
                .build();

        String requestBody = objectMapper.writeValueAsString(contentUpdate);

        // expected
        mockMvc.perform(patch("/content/{cartoonId}/{contentEpisode}",
                        cartoon.getId(), content.getEpisode())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.subTitle").value(SUBTITLE_VALID_MESSAGE))
                .andExpect(jsonPath("$.validation.episode").value(EPISODE_VALID_MESSAGE))
                .andExpect(jsonPath("$.validation.registrationDate").value(REGISTRATION_DATE_VALID_MESSAGE))
                .andDo(document("content/update/400"));
    }

    @Test
    @DisplayName("로그인을 하지 않으면 컨텐츠를 수정할 수 없습니다 - 실패")
    void update401() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .registrationDate(LocalDate.of(1999, 3, 20))
                .build();

        String requestBody = objectMapper.writeValueAsString(contentUpdate);

        // expected
        mockMvc.perform(patch("/content/{cartoonId}/{contentEpisode}",
                        cartoon.getId(), content.getEpisode())
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(AUTHOR_UNAUTHORIZED.getValue()))
                .andDo(document("content/update/401"));
    }

    @Test
    @DisplayName("로그인을 하더라도 작가의 만화가 아니면 접근 권한 예외가 발생합니다 - 실패")
    void update403() throws Exception {
        // given
        Author author = saveAuthorInRepository();

        Author anotherAuthor = Author.builder()
                .nickname("다른 작가 이름")
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        authorRepository.save(anotherAuthor);
        Cartoon cartoon = saveCartoonInRepository(anotherAuthor);
        Content content = saveContentInRepository(cartoon);
        MockHttpSession session = loginAuthorSession(author);

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .registrationDate(LocalDate.of(1999, 3, 20))
                .build();

        String updateJson = objectMapper.writeValueAsString(contentUpdate);

        // expected
        mockMvc.perform(patch("/content/{cartoonId}/{contentEpisode}",
                        cartoon.getId(), content.getEpisode())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode").value(FORBIDDEN))
                .andExpect(jsonPath("$.message").value(CARTOON_FORBIDDEN.getValue()))
                .andDo(document("content/update/403"));
    }

    @Test
    @DisplayName("만화의 에피소드가 없으면 수정에 실패합니다 - 실패")
    void update404() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        saveContentInRepository(cartoon);
        MockHttpSession session = loginAuthorSession(author);

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .registrationDate(LocalDate.of(1999, 3, 20))
                .build();

        String updateJson = objectMapper.writeValueAsString(contentUpdate);

        // expected
        mockMvc.perform(patch("/content/{cartoonId}/{contentEpisode}",
                        cartoon.getId(), 9999L)
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(NOT_FOUND))
                .andExpect(jsonPath("$.message").value(CONTENT_NOT_FOUND.getValue()))
                .andDo(document("content/update/404"));
    }
}