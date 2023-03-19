package com.webtoon.util;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.comment.domain.Comment;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.time.LocalDate;

import static com.webtoon.util.constant.ConstantCommon.*;

public class DomainTest {

    protected PasswordEncoder passwordEncoder = new SCryptPasswordEncoder();

    protected static Author getAuthor() {
        return Author.builder()
                .nickname("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();
    }

    protected static Cartoon getCartoon() {
        return Cartoon.builder()
                .title("만화 제목")
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .build();
    }

    protected static Content getContent() {
        return Content.builder()
                .subTitle("만화 부제")
                .episode(1)
                .rating(9.8)
                .registrationDate(LocalDate.now().minusWeeks(1))
                .build();
    }

    protected static Member getMember() {
        return Member.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .coin(ZERO_OF_TYPE_LONG)
                .build();
    }

    protected static Comment getComment(Member member) {
        return Comment.builder()
                .member(member)
                .commentContent("댓글 내용입니다")
                .build();
    }

    protected static AuthorSession getAuthorSessionFromAuthor(Author author) {
        return AuthorSession.builder()
                .id(author.getId())
                .nickname(author.getNickname())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();
    }

    protected static AuthorSession getAuthorSession() {
        return AuthorSession.builder()
                .nickname("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();
    }
}
