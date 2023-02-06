package com.webtoon.contentmember.domain;

import com.webtoon.content.domain.Content;
import com.webtoon.contentmember.dto.requeset.ContentMemberSave;
import com.webtoon.member.domain.Member;
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
public class ContentMember {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "content_member_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ContentMember(Content content, Member member) {
        this.content = content;
        this.member = member;
    }

    public static ContentMember getFromContentAndMember(Content content, Member member) {
        return ContentMember.builder()
                .content(content)
                .member(member)
                .build();
    }
}
