package com.webtoon.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.author.service.AuthorService;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoon.service.CartoonService;
import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.content.service.ContentService;
import com.webtoon.contentimg.repository.ContentImgRepository;
import com.webtoon.contentimg.service.ContentImgService;
import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.repository.MemberRepository;
import com.webtoon.member.service.MemberService;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthorService authorService;

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected CartoonService cartoonService;

    @Autowired
    protected CartoonRepository cartoonRepository;

    @Autowired
    protected ContentService contentService;

    @Autowired
    protected ContentRepository contentRepository;

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ContentImgService contentImgService;

    @Autowired
    protected ContentImgRepository contentImgRepository;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    protected Author saveAuthorInRepository() {
        Author author = Author.builder()
                .nickName("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        authorRepository.save(author);
        return author;
    }

    protected Cartoon saveCartoonInRepository(Author author) {
        Cartoon cartoon = Cartoon.builder()
                .title("만화 제목")
                .author(author)
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .build();
        cartoonRepository.save(cartoon);
        return cartoon;
    }

    protected Content saveContentInRepository(Cartoon cartoon) {
        Content content = Content.builder()
                .cartoon(cartoon)
                .subTitle("만화 부제")
                .episode(1)
                .rating(9.8f)
                .registrationDate(LocalDate.of(2023, 1, 20))
                .build();

        contentRepository.save(content);
        return content;
    }

    protected Member saveMemberInRepository() {
        Member member = Member.builder()
                .nickName("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();

        memberRepository.save(member);
        return member;
    }

    protected MockHttpSession loginAuthorSession(Author author) throws Exception {
        AuthorLogin authorLogin = AuthorLogin.builder()
                .email(author.getEmail())
                .password(author.getPassword())
                .build();

        String loginJson = objectMapper.writeValueAsString(authorLogin);

        MockHttpServletRequest request = mockMvc.perform(post("/author/login")
                        .contentType(APPLICATION_JSON)
                        .content(loginJson))
                .andReturn().getRequest();

        HttpSession session = request.getSession();
        return (MockHttpSession)session;
    }

    protected MockHttpSession loginMemberSession(Member member) throws Exception {
        MemberLogin memberLogin = MemberLogin.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();

        String loginJson = objectMapper.writeValueAsString(memberLogin);

        MockHttpServletRequest request = mockMvc.perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(loginJson))
                .andReturn().getRequest();

        HttpSession session = request.getSession();
        return (MockHttpSession)session;
    }
}
