package com.webtoon.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCharge {

    @Min(value = 0, message = "0코인 이상을 충전해주세요")
    private long chargeAmount;
}
