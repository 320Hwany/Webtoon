package com.webtoon.cartoonmember.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
        mockMvc.perform(post("/read/{cartoonId}", cartoon.getId())
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
        mockMvc.perform(post("/thumbsUp/{cartoonId}", cartoon.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/thumbsUp/200"));
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
        mockMvc.perform(post("/cartoonMember/like/{memberId}", member.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/like/200"));
    }
}