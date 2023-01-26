package com.webtoon.util;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;

import java.time.LocalDate;

public class DomainTest {

    protected static Author getAuthor() {
        Author author = Author.builder()
                .nickName("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();
        return author;
    }

    protected static Cartoon getCartoon(Author author) {
        Cartoon cartoon = Cartoon.builder()
                .title("만화 제목")
                .author(author)
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .build();
        return cartoon;
    }

    protected static Content getContent(Cartoon cartoon) {
        Content content = Content.builder()
                .cartoon(cartoon)
                .subTitle("만화 부제")
                .episode(1)
                .rating(9.8f)
                .registrationDate(LocalDate.of(2023, 1, 20))
                .build();
        return content;
    }

    protected static Member getMember() {
        Member member = Member.builder()
                .nickName("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .build();
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
