package com.webtoon.member.domain;

import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.util.BaseTimeEntity;
import com.webtoon.util.constant.ConstantValid;
import com.webtoon.util.enumerated.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.time.LocalDate;

import static com.webtoon.util.constant.ConstantCommon.PAYCOIN;
import static com.webtoon.util.constant.ConstantValid.YEAR_MONTH_DAY;
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

    @DateTimeFormat(pattern = YEAR_MONTH_DAY)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private long coin;

    @Builder
    public Member(String nickname, String email, String password, LocalDate birthDate, Gender gender, long coin) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
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
