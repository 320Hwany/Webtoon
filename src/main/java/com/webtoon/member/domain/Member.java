package com.webtoon.member.domain;

import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickName;

    private String email;

    private String password;

    private int coin;

    @Builder
    public Member(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public void update(MemberUpdate memberUpdate) {
        this.nickName = memberUpdate.getNickName();
        this.email = memberUpdate.getEmail();
        this.password = memberUpdate.getPassword();
    }

    public void chargeCoin(int chargeAmount) {
        coin += chargeAmount;
    }

    public static Member getFromMemberSignup(MemberSignup memberSignup) {
        return Member.builder()
                .nickName(memberSignup.getNickName())
                .email(memberSignup.getEmail())
                .password(memberSignup.getPassword())
                .build();
    }
}
