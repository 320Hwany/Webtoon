package com.webtoon.comment.application;

import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.dto.CommentSave;
import com.webtoon.comment.repository.CommentRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        Comment comment = commentService.save(commentSave, 1L, 2L);

        // then
        assertThat(comment.getCommentContent()).isEqualTo("댓글 내용입니다");
        assertThat(comment.getMember().getNickname()).isEqualTo("회원");
        assertThat(comment.getContent().getSubTitle()).isEqualTo("부제");
    }
}