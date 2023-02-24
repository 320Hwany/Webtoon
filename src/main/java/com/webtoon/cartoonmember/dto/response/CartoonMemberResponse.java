package com.webtoon.cartoonmember.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CartoonMemberResponse {

    private String title;

    private String nickname;

    private LocalDateTime lastReadDate;

    @Builder
    @QueryProjection
    public CartoonMemberResponse(String title, String nickname, LocalDateTime lastReadDate) {
        this.title = title;
        this.nickname = nickname;
        this.lastReadDate = lastReadDate;
    }
}
