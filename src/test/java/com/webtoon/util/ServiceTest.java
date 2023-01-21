package com.webtoon.util;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class ServiceTest {

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected CartoonRepository cartoonRepository;

    @Autowired
    protected ContentRepository contentRepository;

    @Autowired
    protected MemberRepository memberRepository;

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

    protected static AuthorSession getAuthorSessionFromAuthor(Author author) {
        AuthorSession authorSession = AuthorSession.builder()
                .id(author.getId())
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();
        return authorSession;
    }
}
