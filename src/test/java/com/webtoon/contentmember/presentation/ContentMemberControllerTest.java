package com.webtoon.contentmember.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContentMemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("회원이 만화 내용을 읽습니다 - 성공")
    void memberReadContent200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(post("/contentMember/read/{contentId}", content.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("contentMember/read/200"));
    }
}