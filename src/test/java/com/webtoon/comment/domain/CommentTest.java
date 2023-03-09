package com.webtoon.comment.domain;

import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.member.domain.Member;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CommentTest extends DomainTest {

    @Test
    @DisplayName("댓글이 수정됩니다")
    void update() {
        // given
        Member member = getMember();
        Comment comment = getComment(member);
        CommentUpdate commentUpdate = CommentUpdate.builder()
                .commentContent("수정 댓글 내용입니다")
                .build();

        // when
        comment.update(commentUpdate);

        // then
        assertThat(comment.getCommentContent()).isEqualTo("수정 댓글 내용입니다");
    }
}