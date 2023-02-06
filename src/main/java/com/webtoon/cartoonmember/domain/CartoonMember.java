package com.webtoon.cartoonmember.domain;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.member.domain.Member;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static java.lang.Boolean.*;
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

    private Boolean thumbsUp;

    @Builder
    public CartoonMember(Cartoon cartoon, Member member, Boolean thumbsUp) {
        this.cartoon = cartoon;
        this.member = member;
        this.thumbsUp = thumbsUp;
    }

    public static CartoonMember getFromCartoonAndMember(Cartoon cartoon, Member member) {
        return CartoonMember.builder()
                .cartoon(cartoon)
                .member(member)
                .thumbsUp(FALSE)
                .build();
    }

    public void thumbsUp() {
        this.thumbsUp = TRUE;
    }
}
