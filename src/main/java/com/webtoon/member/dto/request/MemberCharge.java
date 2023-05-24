package com.webtoon.member.dto.request;

import lombok.*;

import javax.validation.constraints.Min;

import static com.webtoon.util.constant.ConstantValid.CHARGE_VALID_MESSAGE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCharge {

    @Min(value = 0, message = CHARGE_VALID_MESSAGE)
    private long chargeAmount;

    @Builder
    private MemberCharge(long chargeAmount) {
        this.chargeAmount = chargeAmount;
    }
}
