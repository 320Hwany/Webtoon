package com.webtoon.member.domain;

import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private long coin;

    @Builder
    public Member(Long id, String nickname, String email, String password, LocalDate birthDate, long coin) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.coin = coin;
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
