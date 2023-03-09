package com.webtoon.comment.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.comment.dto.CommentSave;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest extends ControllerTest {

    @Test
    @DisplayName("로그인한 회원이 만화 컨텐츠에 댓글을 작성합니다")
    void save() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        CommentSave commentSave = CommentSave.builder()
                .commentContent("댓글 내용입니다")
                .build();

        String jsonBody = objectMapper.writeValueAsString(commentSave);

        // expected
        mockMvc.perform(post("/comment/{contentId}", content.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());
    }
}