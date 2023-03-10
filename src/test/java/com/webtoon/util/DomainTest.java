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
        Author author = Author.builder()
                .nickname("작가 닉네임")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();
        return author;
    }

    protected static Cartoon getCartoon() {
        Cartoon cartoon = Cartoon.builder()
                .title("만화 제목")
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .genre(Genre.ROMANCE)
                .build();
        return cartoon;
    }

    protected static Content getContent() {
        Content content = Content.builder()
                .subTitle("만화 부제")
                .episode(1)
                .rating(9.8)
                .registrationDate(LocalDate.now().minusWeeks(1))
                .build();
        return content;
    }

    protected static Member getMember() {
        Member member = Member.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password("1234")
                .coin(ZERO_OF_TYPE_LONG)
                .build();
        return member;
    }

    protected static Comment getComment(Member member) {
        Comment comment = Comment.builder()
                .member(member)
                .commentContent("댓글 내용입니다")
                .build();

        return comment;
    }

    protected static AuthorSession getAuthorSessionFromAuthor(Author author) {
        AuthorSession authorSession = AuthorSession.builder()
                .id(author.getId())
                .nickname(author.getNickname())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();
        return authorSession;
    }
}
