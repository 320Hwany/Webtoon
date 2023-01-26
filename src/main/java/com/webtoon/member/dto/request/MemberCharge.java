package com.webtoon.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor
public class MemberCharge {

    @Min(value = 0, message = "0코인 이상을 충전해주세요")
    private Integer chargeAmount;

    @Builder
    public MemberCharge(Integer chargeAmount) {
        this.chargeAmount = chargeAmount;
    }
}
