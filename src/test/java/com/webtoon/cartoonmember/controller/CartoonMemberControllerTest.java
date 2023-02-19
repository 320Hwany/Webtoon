package com.webtoon.cartoonmember.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class CartoonMemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("사용자가 만화를 읽으면 CartoonMember 연결 테이블에 회원 - 만화 정보가 추가됩니다")
    void memberReadCartoon() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(post("/cartoonMember/read/{cartoonId}", cartoon.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/read/200"));
    }

    @Test
    @DisplayName("사용자가 만화를 읽으면 CartoonMember 연결 테이블에 회원 - 만화 정보가 추가됩니다")
    void thumbsUp() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(post("/cartoonMember/thumbsUp/{cartoonId}", cartoon.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/thumbsUp/200"));
    }

    @Test
    @DisplayName("회원의 좋아요 목록을 찾습니다")
    void findAllForMember() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(get("/cartoonMember/member")
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/member/200"));
    }

    @Test
    @DisplayName("회원의 좋아요 목록을 찾습니다")
    void findLikeListForMember() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(get("/cartoonMember/member/likeList")
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/member/likeList/200"));
    }

    @Test
    @DisplayName("회원이 만화 평점을 매깁니다")
    void rating() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(post("/cartoonMember/rating/{cartoonId}/{rating}",
                        cartoon.getId(), 9.82)
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/rating/200"));
    }
}