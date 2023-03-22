package com.webtoon.member.presentation;

import com.webtoon.member.domain.Member;
import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.exception.MemberNotFoundException;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.time.LocalDate;

import static com.webtoon.util.constant.ConstantCommon.*;
import static com.webtoon.util.constant.ConstantValid.*;
import static com.webtoon.util.enumerated.ErrorMessage.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("조건이 맞으면 회원가입이 됩니다 - 성공")
    void signup200() throws Exception {
        // given
        MemberSignup memberSignup = MemberSignup.builder()
                .nickname("회원 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .gender("MAN")
                .build();

        String requestBody = objectMapper.writeValueAsString(memberSignup);

        // expected
        mockMvc.perform(post("/member/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(document("member/signup/200"));
    }

    @Test
    @DisplayName("회원가입 조건이 맞지 않으면 예외가 발생합니다- 성공")
    void signup400() throws Exception {
        // given
        MemberSignup memberSignup = MemberSignup.builder()
                .nickname("")
                .email("yhwjd99")
                .password("")
                .build();

        String requestBody = objectMapper.writeValueAsString(memberSignup);

        // expected
        mockMvc.perform(post("/member/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.nickname").value(NICKNAME_VALID_MESSAGE))
                .andExpect(jsonPath("$.validation.password").value(PASSWORD_VALID_MESSAGE))
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

        String requestBody = objectMapper.writeValueAsString(memberLogin);

        // expected
        mockMvc.perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(member.getNickname()))
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andDo(document("member/login/200"));
    }

    @Test
    @DisplayName("조건이 맞지 않으면 로그인을 할 수 없습니다 - 실패")
    void login400_BADREQUEST() throws Exception {
        // given
        saveMemberInRepository();

        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd")
                .password("")
                .build();

        String requestBody = objectMapper.writeValueAsString(memberLogin);

        // expected
        mockMvc.perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.password").value(PASSWORD_VALID_MESSAGE))
                .andDo(document("member/login/400/badRequest"));
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 로그인을 할 수 없습니다 - 실패")
    void login400_NOTMATCH() throws Exception {
        // given
        saveMemberInRepository();

        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd@naver.com")
                .password("4321")
                .build();

        String requestBody = objectMapper.writeValueAsString(memberLogin);

        // expected
        mockMvc.perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(MEMBER_NOT_MATCH.getValue()))
                .andDo(document("member/login/400/notMatch"));
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 로그인을 할 수 없습니다 - 실패")
    void login404() throws Exception {
        // given
        MemberLogin memberLogin = MemberLogin.builder()
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        String requestBody = objectMapper.writeValueAsString(memberLogin);

        // expected
        mockMvc.perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(NOT_FOUND))
                .andExpect(jsonPath("$.message").value(MEMBER_NOT_FOUND.getValue()))
                .andDo(document("member/login/404"));
    }

    @Test
    @DisplayName("존재하는 회원이면 회원 수정이 됩니다 - 성공")
    void update200() throws Exception {
        // given
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickname("수정 닉네임")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        String requestBody = objectMapper.writeValueAsString(memberUpdate);

        // expected
        mockMvc.perform(patch("/member")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("수정 닉네임"))
                .andExpect(jsonPath("$.email").value("yhwjd@naver.com"))
                .andDo(document("member/update/200"));
    }

    @Test
    @DisplayName("회원 조건이 맞지 않으면 회원 수정을 할 수 없습니다 - 실패")
    void update400() throws Exception {
        // given
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickname("")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        String requestBody = objectMapper.writeValueAsString(memberUpdate);

        // expected
        mockMvc.perform(patch("/member")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.nickname").value(NICKNAME_VALID_MESSAGE))
                .andDo(document("member/update/400"));
    }

    @Test
    @DisplayName("로그인을 하지 않으면 회원정보를 수정할 수 없습니다 - 실패")
    void update401() throws Exception {
        // given
        saveMemberInRepository();

        MemberUpdate memberUpdate = MemberUpdate.builder()
                .nickname("수정 닉네임")
                .email("yhwjd@naver.com")
                .password("123456789")
                .build();

        String requestBody = objectMapper.writeValueAsString(memberUpdate);

        // expected
        mockMvc.perform(patch("/member")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(MEMBER_UNAUTHORIZED.getValue()))
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

    @Test
    @DisplayName("로그인을 하지 않으면 회원을 삭제할 수 없습니다")
    void delete401() throws Exception {
        // given
        saveMemberInRepository();

        // expected
        mockMvc.perform(delete("/member")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(MEMBER_UNAUTHORIZED.getValue()))
                .andDo(document("member/delete/401"));
    }

    @Test
    @DisplayName("로그아웃에 성공합니다")
    void logout200() throws Exception {
        // given
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(post("/member/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("member/logout/200"));
    }

    @Test
    @DisplayName("존재하는 회원이고 조건에 맞으면 충전이 가능합니다 - 성공")
    void charge200() throws Exception {
        // given
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        MemberCharge memberCharge = MemberCharge.builder()
                .chargeAmount(10000L)
                .build();

        String requestBody = objectMapper.writeValueAsString(memberCharge);

        // expected
        mockMvc.perform(post("/member/charge")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(document("member/charge/200"));
    }

    @Test
    @DisplayName("조건에 맞지 않으면 충전이 되지 않습니다 - 실패")
    void charge400() throws Exception {
        // given
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        MemberCharge memberCharge = MemberCharge.builder()
                .chargeAmount(-10000L)
                .build();

        String chargeJson = objectMapper.writeValueAsString(memberCharge);

        // expected
        mockMvc.perform(post("/member/charge")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(chargeJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(VALID_BAD_REQUEST.getValue()))
                .andExpect(jsonPath("$.validation.chargeAmount").value(CHARGE_VALID_MESSAGE))
                .andDo(document("member/charge/400"));
    }

    @Test
    @DisplayName("로그인을 하지 않으면 충전을 할 수 없습니다 - 실패")
    void charge401() throws Exception {
        // given
        MemberCharge memberCharge = MemberCharge.builder()
                .chargeAmount(10000L)
                .build();

        String chargeJson = objectMapper.writeValueAsString(memberCharge);

        // expected
        mockMvc.perform(post("/member/charge")
                        .contentType(APPLICATION_JSON)
                        .content(chargeJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(UNAUTHORIZED))
                .andExpect(jsonPath("$.message").value(MEMBER_UNAUTHORIZED.getValue()))
                .andDo(document("member/charge/401"));
    }
}