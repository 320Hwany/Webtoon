package com.webtoon.content.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ContentControllerTest extends ControllerTest {

    @Test
    @DisplayName("작가로 로그인한 후 자신의 만화에 내용을 추가할 수 있습니다 - 성공")
    void save200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        MockHttpSession session = loginAuthorSession();

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제 입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        String contentSaveJson = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(contentSaveJson))
                .andExpect(status().isOk())
                .andDo(document("content/save/200"));
    }

    @Test
    @DisplayName("ContentSave가 조건에 맞지 않으면 예외가 발생합니다 - 실패")
    void save400() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        MockHttpSession session = loginAuthorSession();

        ContentSave contentSave = ContentSave.builder()
                .subTitle("")
                .episode(-1)
                .registrationDate(LocalDate.of(2023, 1, 19))
                .build();

        String contentSaveJson = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(contentSaveJson))
                .andExpect(status().isBadRequest())
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

        String contentSaveJson = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", cartoon.getId())
                        .contentType(APPLICATION_JSON)
                        .content(contentSaveJson))
                .andExpect(status().isUnauthorized())
                .andDo(document("content/save/401"));
    }

    @Test
    @DisplayName("로그인을 하더라도 작가의 만화가 아니면 접근 권한 예외가 발생합니다 - 실패")
    void save403() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();

        Author anotherAuthor = Author.builder()
                .nickName("다른 작가 이름")
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
        String contentSaveJson = objectMapper.writeValueAsString(contentSave);

        // expected
        mockMvc.perform(post("/content/{cartoonId}", cartoon.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(contentSaveJson))
                .andExpect(status().isForbidden())
                .andDo(document("content/save/403"));
    }

    @Test
    @DisplayName("로그인을 하더라도 만화를 찾을 수 없으면 만화 내용을 등록할 수 없습니다 - 실패")
    void save404() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        MockHttpSession session = loginAuthorSession();

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
                .andDo(document("content/save/404"));
    }
}