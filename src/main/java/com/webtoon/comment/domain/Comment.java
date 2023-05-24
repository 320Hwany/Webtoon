package com.webtoon.comment.domain;

import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime lastModifiedDateTime;

    @Builder
    private Comment(String commentContent, Member member, Content content, long likes,
                   LocalDateTime createDateTime, LocalDateTime lastModifiedDateTime) {
        this.commentContent = commentContent;
        this.member = member;
        this.content = content;
        this.likes = likes;
        this.createDateTime = createDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public void update(CommentUpdate commentUpdate) {
        this.commentContent = commentUpdate.getCommentContent();
    }

    public Long getMemberId() {
        return member.getId();
    }
}
