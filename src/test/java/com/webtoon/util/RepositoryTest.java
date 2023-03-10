package com.webtoon.util;

import com.webtoon.author.domain.Author;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.repository.CommentRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import com.webtoon.contentImgInfo.repository.ContentImgInfoRepository;
import com.webtoon.contentmember.repository.ContentMemberRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_DOUBLE;
import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_LONG;

@AcceptanceTest
public class RepositoryTest {

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected CartoonRepository cartoonRepository;

    @Autowired
    protected ContentRepository contentRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ContentImgInfoRepository contentImgInfoRepository;

    @Autowired
    protected CartoonMemberRepository cartoonMemberRepository;

    @Autowired
    protected ContentMemberRepository contentMemberRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;


    protected Author saveAuthorInRepository() {
        Author author = Author.builder()
                .nickname("작가 이름")
                .email("yhwjd99@gmail.com")
                .password(passwordEncoder.encode("1234"))
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
                .rating(ZERO_OF_TYPE_DOUBLE)
                .likes(ZERO_OF_TYPE_LONG)
                .build();

        author.getCartoonList().add(cartoon);
        cartoonRepository.save(cartoon);
        return cartoon;
    }

    protected Content saveContentInRepository(Cartoon cartoon) {
        Content content = Content.builder()
                .cartoon(cartoon)
                .subTitle("만화 부제")
                .episode(1)
                .rating(9.8)
                .registrationDate(LocalDate.now())
                .build();

        contentRepository.save(content);
        return content;
    }

    protected ContentImgInfo saveContentImgInfoInRepository(Content content) {
        ContentImgInfo contentImgInfo = ContentImgInfo.builder()
                .imgName("hello.txt")
                .content(content)
                .build();

        contentImgInfoRepository.save(contentImgInfo);
        return contentImgInfo;
    }

    protected Member saveMemberInRepository() {
        Member member = Member.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(ZERO_OF_TYPE_LONG)
                .birthDate(LocalDate.of(1999,03,20))
                .build();

        memberRepository.save(member);
        return member;
    }

    protected CartoonMember saveCartoonMemberInRepository(Cartoon cartoon, Member member) {
        CartoonMember cartoonMember = CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(false)
                .build();

        return cartoonMemberRepository.save(cartoonMember);
    }

    protected Comment saveCommentInRepository(Content content, Member member) {
        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .commentContent("댓글 내용입니다")
                .build();

        return commentRepository.save(comment);
    }
}
