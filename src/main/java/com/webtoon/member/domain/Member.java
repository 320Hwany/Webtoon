package com.webtoon.member.domain;

import com.webtoon.content.domain.Content;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.util.BaseTimeEntity;
import com.webtoon.util.constant.Constant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.PAYCOIN;
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
        this.coin += chargeAmount;
    }

    public void validatePreviewContent(LocalDate lockLocalDate) {
        if (lockLocalDate.isAfter(LocalDate.now())) {
            if (this.coin < 200) {
                throw new LackOfCoinException();
            }
            this.coin -= PAYCOIN;
        }
    }

    public static Member getFromMemberSignup(MemberSignup memberSignup) {
        return Member.builder()
                .nickName(memberSignup.getNickName())
                .email(memberSignup.getEmail())
                .password(memberSignup.getPassword())
                .build();
    }
}
