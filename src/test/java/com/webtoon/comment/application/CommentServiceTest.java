package com.webtoon.comment.application;

import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.dto.request.CommentSave;
import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.comment.dto.response.CommentResponse;
import com.webtoon.comment.repository.CommentRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ContentRepository contentRepository;

    private static final Long ANY_ID = 1L;

    @Test
    @DisplayName("회원, 만화내용, 댓글 내용으로 댓글을 저장합니다")
    void save() {
        // given
        CommentSave commentSave = CommentSave.builder()
                .commentContent("댓글 내용입니다")
                .build();

        Member member = Member.builder()
                .nickname("회원")
                .build();

        Content content = Content.builder()
                .subTitle("부제")
                .build();

        // stub 1
        when(memberRepository.getById(any())).thenReturn(member);
        when(contentRepository.getById(any())).thenReturn(content);
        when(commentRepository.save(any())).thenReturn(commentSave.toEntity(member, content));

        // when
        CommentResponse commentResponse = commentService.save(commentSave, ANY_ID, ANY_ID);

        // then
        assertThat(commentResponse.getCommentContent()).isEqualTo("댓글 내용입니다");
        assertThat(commentResponse.getNickname()).isEqualTo("회원");
    }

    @Test
    @DisplayName("댓글 내용을 수정합니다")
    void update() {
        // given
        Member member = Member.builder()
                .nickname("회원 닉네임")
                .build();

        Comment comment = Comment.builder()
                .commentContent("댓글 내용입니다")
                .member(member)
                .build();

        CommentUpdate commentUpdate = CommentUpdate.builder()
                .commentContent("수정 댓글 내용입니다")
                .build();

        // stub 1
        when(commentRepository.getById(any())).thenReturn(comment);

        // when
        CommentResponse commentResponse = commentService.update(ANY_ID, commentUpdate);

        // then
        assertThat(commentResponse.getCommentContent()).isEqualTo("수정 댓글 내용입니다");
        assertThat(commentResponse.getNickname()).isEqualTo("회원 닉네임");
    }
}