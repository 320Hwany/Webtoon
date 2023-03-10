package com.webtoon.comment.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Lob
    private String commentContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    private long likes;

    @Builder
    public Comment(String commentContent, Member member, Content content, long likes) {
        this.commentContent = commentContent;
        this.member = member;
        this.content = content;
        this.likes = likes;
    }

    public void update(CommentUpdate commentUpdate) {
        this.commentContent = commentUpdate.getCommentContent();
    }

    public Long getMemberId() {
        return member.getId();
    }
}
