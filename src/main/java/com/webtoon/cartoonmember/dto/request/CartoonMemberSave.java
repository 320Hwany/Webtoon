package com.webtoon.cartoonmember.dto.request;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartoonMemberSave {

    private Cartoon cartoon;
    private Member member;

    @Builder
    public CartoonMemberSave(Cartoon cartoon, Member member) {
        this.cartoon = cartoon;
        this.member = member;
    }

    public static CartoonMemberSave getFromCartoonAndMember(Cartoon cartoon, Member member) {
        return CartoonMemberSave.builder()
                .cartoon(cartoon)
                .member(member)
                .build();
    }
}
