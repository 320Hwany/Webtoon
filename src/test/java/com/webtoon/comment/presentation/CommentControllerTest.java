package com.webtoon.comment.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.dto.request.CommentSave;
import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ControllerTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

        assertThat(commentRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인한 회원이 만화 컨텐츠에 댓글을 수정합니다")
    void update200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        Comment comment = saveCommentInRepository(content, member);
        MockHttpSession session = loginMemberSession(member);

        CommentUpdate commentUpdate = CommentUpdate.builder()
                .commentContent("수정 댓글 내용입니다")
                .build();

        String jsonBody = objectMapper.writeValueAsString(commentUpdate);

        // expected
        mockMvc.perform(patch("/comment/{commentId}", comment.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("회원 닉네임"))
                .andExpect(jsonPath("$.commentContent").value("수정 댓글 내용입니다"))
                .andExpect(jsonPath("$.commentId").value(comment.getId()));
    }

    @Test
    @DisplayName("로그인하지 않으면 댓글을 수정할 수 없습니다")
    void update401() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        Comment comment = saveCommentInRepository(content, member);

        CommentUpdate commentUpdate = CommentUpdate.builder()
                .commentContent("수정 댓글 내용입니다")
                .build();

        String jsonBody = objectMapper.writeValueAsString(commentUpdate);

        // expected
        mockMvc.perform(patch("/comment/{commentId}", comment.getId())
                        .contentType(APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value("401"))
                .andExpect(jsonPath("$.message").value("로그인 후 이용해주세요"));
    }
}