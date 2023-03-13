package com.webtoon.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

import static com.webtoon.util.constant.ConstantValid.CHARGE_VALID_MESSAGE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCharge {

    @Min(value = 0, message = CHARGE_VALID_MESSAGE)
    private long chargeAmount;
}
