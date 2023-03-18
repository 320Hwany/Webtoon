package com.webtoon.cartoonmember.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ControllerTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static com.webtoon.util.constant.ConstantCommon.ZERO_OF_TYPE_DOUBLE;
import static com.webtoon.util.constant.ConstantCommon.ZERO_OF_TYPE_LONG;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartoonMemberControllerTest extends ControllerTest {

//    @Test
//    @DisplayName("사용자가 만화를 읽으면 CartoonMember 연결 테이블에 회원 - 만화 정보가 추가됩니다")
//    void memberReadCartoon() throws Exception {
//        // given
//        Author author = saveAuthorInRepository();
//        Cartoon cartoon = saveCartoonInRepository(author);
//        Member member = saveMemberInRepository();
//        MockHttpSession session = loginMemberSession(member);
//
//        // expected
//        mockMvc.perform(post("/cartoonMember/read/{cartoonId}", cartoon.getId())
//                        .session(session))
//                .andExpect(status().isOk())
//                .andDo(document("cartoonMember/read/200"));
//    }

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
    @DisplayName("연령대별 인기 만화를 인기순으로 보여줍니다")
    void findAllByMemberAge() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);

        // expected
        mockMvc.perform(get("/cartoonMember/ageRange")
                        .param("page", "0")
                        .param("size", "10")
                        .param("ageRange", "20"))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/ageRange/200"));
    }

    @Test
    @DisplayName("선택한 성별의 인기 웹툰을 보여줍니다")
    void findAllByMemberGender() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        saveCartoonMemberInRepository(cartoon, member);

        Cartoon anotherCartoon = Cartoon.builder()
                .title("다른 만화 제목")
                .author(author)
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .rating(ZERO_OF_TYPE_DOUBLE)
                .likes(ZERO_OF_TYPE_LONG)
                .build();

        cartoonRepository.save(anotherCartoon);
        saveCartoonMemberInRepository(cartoon, member);
        saveCartoonMemberInRepository(anotherCartoon, member);

        // expected
        mockMvc.perform(get("/cartoonMember/gender")
                        .param("page", "0")
                        .param("size", "10")
                        .param("gender", "MAN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.cartoonResponseList[0].title").value("만화 제목"))
                .andExpect(jsonPath("$.cartoonResponseList[1].title").value("다른 만화 제목"))
                .andDo(document("cartoonMember/gender/200"));
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