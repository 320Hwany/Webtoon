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

import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_LONG;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    @DisplayName("해당 댓글이 본인 댓글이 아니라 접근 권한 예외가 발생합니다")
    void update403() throws Exception {
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

        Member anotherMember = Member.builder()
                .nickname("다른 회원 닉네임")
                .email("다른회원@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(ZERO_OF_TYPE_LONG)
                .build();

        memberRepository.save(anotherMember);
        MockHttpSession session = loginMemberSession(anotherMember);

        // expected
        mockMvc.perform(patch("/comment/{commentId}", comment.getId())
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode").value("403"))
                .andExpect(jsonPath("$.message").value("해당 댓글에 접근 권한이 없습니다"));
    }

    @Test
    @DisplayName("해당 댓글이 없다면 예외가 발생합니다")
    void update404() throws Exception {
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
        mockMvc.perform(patch("/comment/{commentId}", 9999L)
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다"));
    }

    @Test
    @DisplayName("로그인한 회원이 만화 컨텐츠에 댓글을 작성합니다")
    void delete200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        Comment comment = saveCommentInRepository(content, member);
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(delete("/comment/{commentId}", comment.getId())
                        .session(session))
                .andExpect(status().isOk());

        assertThat(commentRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("로그인하지 않으면 댓글을 삭제할 수 없습니다")
    void delete401() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        Comment comment = saveCommentInRepository(content, member);

        // expected
        mockMvc.perform(delete("/comment/{commentId}", comment.getId()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value("401"))
                .andExpect(jsonPath("$.message").value("로그인 후 이용해주세요"));
    }

    @Test
    @DisplayName("로그인하지 않으면 댓글을 삭제할 수 없습니다")
    void delete403() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        Comment comment = saveCommentInRepository(content, member);

        Member anotherMember = Member.builder()
                .nickname("다른 회원 닉네임")
                .email("다른회원@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(ZERO_OF_TYPE_LONG)
                .build();

        memberRepository.save(anotherMember);
        MockHttpSession session = loginMemberSession(anotherMember);

        // expected
        mockMvc.perform(delete("/comment/{commentId}", comment.getId())
                        .session(session))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode").value("403"))
                .andExpect(jsonPath("$.message").value("해당 댓글에 접근 권한이 없습니다"));
    }

    @Test
    @DisplayName("댓글이 존재하지 않으면 예외가 발생합니다")
    void delete404() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        Comment comment = saveCommentInRepository(content, member);
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(delete("/comment/{commentId}", 9999L)
                        .session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다"));
    }

    @Test
    @DisplayName("로그인한 회원의 모든 댓글을 최신순으로 가져옵니다")
    void findAllForMember() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        Comment comment = saveCommentInRepository(content, member);
        MockHttpSession session = loginMemberSession(member);

        // when
        mockMvc.perform(get("/comment/member")
                        .session(session)
                        .param("page", "0")
                        .param("size","20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.commentResponse[0].commentId").value(comment.getId()))
                .andExpect(jsonPath("$.commentResponse[0].commentContent").value(comment.getCommentContent()))
                .andExpect(jsonPath("$.commentResponse[0].nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.commentResponse[0].likes").value(comment.getLikes()));
    }
}