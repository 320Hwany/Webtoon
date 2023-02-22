package com.webtoon.cartoonmember.domain;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.member.domain.Member;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class CartoonMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cartoon_member_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cartoon_id")
    private Cartoon cartoon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean thumbsUp;

    private boolean rated;

    private LocalDateTime lastReadDate;

    @Builder
    public CartoonMember(Cartoon cartoon, Member member, boolean thumbsUp, boolean rated) {
        this.cartoon = cartoon;
        this.member = member;
        this.thumbsUp = thumbsUp;
        this.rated = rated;
    }

    public static CartoonMember getFromCartoonAndMember(Cartoon cartoon, Member member) {
        return CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(false)
                .rated(false)
                .build();
    }

    public void thumbsUp() {
        this.thumbsUp = true;
    }

    public void rated() {
        this.rated = true;
    }

    public void updateReadDate(LocalDateTime localDateTime) {
        this.lastReadDate = localDateTime;
    }
}
