package com.webtoon.cartoonmember.domain;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class CartoonMember {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cartoon_member_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cartoon_id")
    private Cartoon cartoon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean thumbsUp = false;

    @Builder
    public CartoonMember(Cartoon cartoon, Member member) {
        this.cartoon = cartoon;
        this.member = member;
    }

    public static CartoonMember getFromCartoonMemberSave(CartoonMemberSave cartoonMemberSave) {
        return CartoonMember.builder()
                .cartoon(cartoonMemberSave.getCartoon())
                .member(cartoonMemberSave.getMember())
                .build();
    }
}
