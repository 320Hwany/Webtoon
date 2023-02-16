package com.webtoon.member.domain;

import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.PAYCOIN;
import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_LONG;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private long coin;

    @Builder
    public Member(String nickname, String email, String password, Long coin) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.coin = coin;
    }

    public static Member getFromMemberSignup(MemberSignup memberSignup, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .nickname(memberSignup.getNickname())
                .email(memberSignup.getEmail())
                .password(passwordEncoder.encode(memberSignup.getPassword()))
                .coin(ZERO_OF_TYPE_LONG)
                .build();
    }

    public void update(MemberUpdate memberUpdate, PasswordEncoder passwordEncoder) {
        this.nickname = memberUpdate.getNickname();
        this.email = memberUpdate.getEmail();
        this.password = passwordEncoder.encode(memberUpdate.getPassword());
    }

    public void chargeCoin(long chargeAmount) {
        this.coin += chargeAmount;
    }

    public void validatePreviewContent(LocalDate lockLocalDate) {
        if (lockLocalDate.isAfter(LocalDate.now())) {
            if (this.coin < PAYCOIN) {
                throw new LackOfCoinException();
            }
            this.coin -= PAYCOIN;
        }
    }
}
