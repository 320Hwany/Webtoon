package com.webtoon.comment.repository;

import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.dto.response.CommentResponse;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import com.webtoon.util.RepositoryTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;


class CommentRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("회원의 모든 댓글을 최신순으로 가져옵니다")
    void findAllForMember() {
        // given
        Member member = saveMemberInRepository();

        List<Comment> commentList = IntStream.range(1, 11)
                .mapToObj(i -> Comment.builder()
                        .commentContent("댓글 내용 - " + i)
                        .member(member)
                        .build())
                .collect(Collectors.toList());

        commentRepository.saveAll(commentList);

        List<Comment> anotherCommentList = IntStream.range(11, 20)
                .mapToObj(i -> Comment.builder()
                        .commentContent("댓글 내용 - " + i)
                        .member(null)
                        .build())
                .collect(Collectors.toList());

        commentRepository.saveAll(anotherCommentList);
        // when
        List<CommentResponse> commentResponseList = commentRepository.findAllForMember(member.getId());

        // then
        assertThat(commentResponseList.size()).isEqualTo(10);
    }
}