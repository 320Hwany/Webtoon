package com.webtoon.author.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorTransactionServiceTest extends ServiceTest {

    @Autowired
    private AuthorTransactionService authorTransactionService;

    @Test
    @DisplayName("중복 검증, 회원 가입 트랜잭션 세트를 진행합니다")
    void signupSet() {
        // given
        AuthorSignup authorSignup = AuthorSignup.builder()
                .nickname("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        // when
        authorTransactionService.signupSet(authorSignup);

        // then
        assertThat(authorRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("중복 검증, 회원 가입 트랜잭션 세트를 진행합니다")
    void loginSet() {
        // given
        Author author = saveAuthorInRepository();

        AuthorLogin authorLogin = AuthorLogin.builder()
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        AuthorResponse authorResponse = authorTransactionService.loginSet(authorLogin, httpServletRequest);

        // then
        assertThat(authorResponse.getNickname()).isEqualTo(author.getNickname());
        assertThat(authorResponse.getEmail()).isEqualTo("yhwjd99@gmail.com");
    }

    @Test
    @DisplayName("AuthorId로 작가 회원을 찾고 들어온 정보로 수정합니다")
    void updateSet200() {
        // given
        Author author = saveAuthorInRepository();

        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickname("수정 닉네임")
                .email("수정 이메일")
                .password("4321")
                .build();

        // when
        AuthorResponse authorResponse = authorTransactionService.updateSet(author.getId(), authorUpdate);

        // then
        assertThat(authorResponse.getNickname()).isEqualTo("수정 닉네임");
        assertThat(authorResponse.getEmail()).isEqualTo("수정 이메일");
    }

    @Test
    @DisplayName("AuthorId와 일치하는 작가 회원이 없다면 예외가 발생합니다")
    void updateSet404() {
        // given
        AuthorUpdate authorUpdate = AuthorUpdate.builder()
                .nickname("수정 닉네임")
                .email("수정 이메일")
                .password("4321")
                .build();

        // expected
        Assertions.assertThrows(AuthorNotFoundException.class,
                () -> authorTransactionService.updateSet(9999L, authorUpdate));
    }

    @Test
    @DisplayName("AuthorId로 작가 회원을 찾고 작가를 삭제합니다")
    void deleteSet200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        author.getCartoonList().add(cartoon);
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // when
        authorTransactionService.deleteSet(authorSession, httpServletRequest);

        // then
        assertThat(authorRepository.count()).isEqualTo(0);
        assertThat(cartoonRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("AuthorId와 일치하는 작가가 없다면 예외가 발생합니다")
    void deleteSet404() {
        // given
        AuthorSession authorSession = AuthorSession.builder()
                .id(9999L)
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        // expected
        Assertions.assertThrows(AuthorNotFoundException.class,
                () -> authorTransactionService.deleteSet(authorSession, new MockHttpServletRequest()));
    }

    @Test
    @DisplayName("입력한 닉네임이 포함된 작가를 검색하여 입력한 정보에 맞는 페이지를 반환합니다 - 성공")
    void findAllByNicknameContains() {
        // given
        Author author = saveAuthorInRepository();
        saveCartoonInRepository(author);

        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(0)
                .nickname("작가 이름")
                .dayOfTheWeek("NONE")
                .progress("NONE")
                .genre("NONE")
                .build();

        // when
        List<AuthorCartoonResponse> authorCartoonResponseList =
                authorTransactionService.findAllByNicknameContains(cartoonSearchDto);

        // then
        AuthorCartoonResponse authorCartoonResponse = authorCartoonResponseList.get(0);
        List<CartoonResponse> cartoonResponseList = authorCartoonResponse.getCartoonResponseList();

        assertThat(authorCartoonResponseList.size()).isEqualTo(1);
        assertThat(cartoonResponseList.size()).isEqualTo(1);
    }
}