package com.webtoon.member.controller;

import com.webtoon.member.domain.Member;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class MemberControllerTest extends ControllerTest {


    @Test
    @DisplayName("조건이 맞으면 회원가입이 됩니다 - 성공")
    void signup200() throws Exception {
        // given
        MemberSignup memberSignup = MemberSignup.builder()
                .nickName("회원 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        String memberSignupJson = objectMapper.writeValueAsString(memberSignup);

        // expected
        mockMvc.perform(post("/member/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberSignupJson))
                .andExpect(status().isOk())
                .andDo(document("member/signup/200"));
    }

    @Test
    @DisplayName("회원가입 조건이 맞지 않으면 예외가 발생합니다- 성공")
    void signup400() throws Exception {
        // given
        MemberSignup memberSignup = MemberSignup.builder()
                .nickName("")
                .email("yhwjd99")
                .password("")
                .build();

        String memberSignupJson = objectMapper.writeValueAsString(memberSignup);

        // expected
        mockMvc.perform(post("/member/signup")
                        .contentType(APPLICATION_JSON)
                        .content(memberSignupJson))
                .andExpect(status().isBadRequest())
                .andDo(document("member/signup/400"));
    }

    @Test
    @DisplayName("조건이 맞으면 로그인이 됩니다 - 성공")
    void login200() throws Exception {
        // given
        Member member = saveMemberInRepository();

        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        String memberLoginJson = objectMapper.writeValueAsString(memberLogin);

        // expected
        mockMvc.perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(memberLoginJson))
                .andExpect(status().isOk())
                .andDo(document("member/login/200"));
    }

    @Test
    @DisplayName("조건이 맞지 않으면 로그인을 할 수 없습니다 - 실패")
    void login400() throws Exception {
        // given
        Member member = saveMemberInRepository();

        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd")
                .password("")
                .build();

        String memberLoginJson = objectMapper.writeValueAsString(memberLogin);

        // expected
        mockMvc.perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(memberLoginJson))
                .andExpect(status().isBadRequest())
                .andDo(document("member/login/400"));
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 로그인을 할 수 없습니다 - 실패")
    void login404() throws Exception {
        // given
        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        String memberLoginJson = objectMapper.writeValueAsString(memberLogin);

        // expected
        mockMvc.perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(memberLoginJson))
                .andExpect(status().isNotFound())
                .andDo(document("member/login/404"));
    }

    @Test
    @DisplayName("존재하는 회원이면 회원 수정이 됩니다 - 성공")
    void update200() throws Exception {
        // given
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickName("수정 닉네임")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        String memberUpdateJson = objectMapper.writeValueAsString(memberUpdate);

        // expected
        mockMvc.perform(patch("/member")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(memberUpdateJson))
                .andExpect(status().isOk())
                .andDo(document("member/update/200"));
    }

    @Test
    @DisplayName("회원 조건이 맞지 않으면 회원 수정을 할 수 없습니다 - 실패")
    void update400() throws Exception {
        // given
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickName("")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        String memberUpdateJson = objectMapper.writeValueAsString(memberUpdate);

        // expected
        mockMvc.perform(patch("/member")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(memberUpdateJson))
                .andExpect(status().isBadRequest())
                .andDo(document("member/update/400"));
    }

    @Test
    @DisplayName("로그인을 하지 않으면 회원정보를 수정할 수 없습니다 - 실패")
    void update401() throws Exception {
        // given
        Member member = saveMemberInRepository();

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickName("수정 닉네임")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        String memberUpdateJson = objectMapper.writeValueAsString(memberUpdate);

        // expected
        mockMvc.perform(patch("/member")
                        .contentType(APPLICATION_JSON)
                        .content(memberUpdateJson))
                .andExpect(status().isUnauthorized())
                .andDo(document("member/update/401"));
    }

    @Test
    @DisplayName("존재하는 회원이면 회원이 삭제 됩니다 - 성공")
    void delete200() throws Exception {
        // given
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(delete("/member")
                        .session(session)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/delete/200"));
    }
}