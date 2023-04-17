package com.webtoon.cartoonmember.presentation;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ControllerTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static com.webtoon.util.constant.ConstantCommon.*;
import static com.webtoon.util.enumerated.ErrorMessage.CARTOON_MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartoonMemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("사용자가 좋아요를 누르면 만화 좋아요가 1 증가하고 연결 테이블에 좋아요 표시로 바뀝니다")
    void thumbsUp200() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = saveCartoonMemberInRepository(cartoon, member);
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(post("/cartoonMember/thumbsUp/{cartoonId}", cartoon.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("cartoonMember/thumbsUp/200"));

        Cartoon findCartoon = cartoonRepository.getById(cartoon.getId());
        CartoonMember findCartoonMember = cartoonMemberRepository.getById(cartoonMember.getId());
        long cartoonLikesFromCache = cartoonMemberService.getCartoonLikesFromCache(findCartoon.getId());

        assertThat(cartoonLikesFromCache).isEqualTo(1);
        assertThat(findCartoonMember.isThumbsUp()).isEqualTo(true);
    }

    @Test
    @DisplayName("아직 만화를 보지 않아 연결 테이블에 정보가 없으면 예외가 발생합니다")
    void thumbsUp404() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(post("/cartoonMember/thumbsUp/{cartoonId}", cartoon.getId())
                        .session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(NOT_FOUND))
                .andExpect(jsonPath("$.message").value(CARTOON_MEMBER_NOT_FOUND.getValue()))
                .andDo(document("cartoonMember/thumbsUp/404"));
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
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.cartoonResponseList[0].title").value(cartoon.getTitle()))
                .andExpect(jsonPath("$.cartoonResponseList[0].authorNickname").value(author.getNickname()))
                .andDo(document("cartoonMember/member/200"));
    }

    @Test
    @DisplayName("회원의 좋아요 목록을 찾습니다")
    void findLikeListForMember() throws Exception {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Member member = saveMemberInRepository();
        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(true)
                .rated(false)
                .build();

        cartoonMemberRepository.save(cartoonMember);
        MockHttpSession session = loginMemberSession(member);

        // expected
        mockMvc.perform(get("/cartoonMember/member/likeList")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.cartoonResponseList[0].title").value(cartoon.getTitle()))
                .andExpect(jsonPath("$.cartoonResponseList[0].authorNickname").value(author.getNickname()))
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
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.cartoonResponseList[0].title").value(cartoon.getTitle()))
                .andExpect(jsonPath("$.cartoonResponseList[0].nickname").value(author.getNickname()))
                .andExpect(jsonPath("$.cartoonResponseList[0].likes").value(0))
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