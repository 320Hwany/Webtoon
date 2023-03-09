package com.webtoon.comment.dto.request;

import com.webtoon.comment.domain.Comment;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.util.constant.Constant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommentSave {

    private String commentContent;

    @Builder
    public CommentSave(String commentContent) {
        this.commentContent = commentContent;
    }

    public Comment toEntity(Member member, Content content) {
        return Comment.builder()
                .commentContent(commentContent)
                .member(member)
                .content(content)
                .likes(Constant.ZERO_OF_TYPE_LONG)
                .build();
    }
}
