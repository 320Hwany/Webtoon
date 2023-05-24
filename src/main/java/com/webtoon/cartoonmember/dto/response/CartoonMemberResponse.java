package com.webtoon.cartoonmember.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartoonMemberResponse {

    private String title;

    private String authorNickname;

    private LocalDateTime lastReadDate;

    @Builder
    @QueryProjection
    public CartoonMemberResponse(String title, String authorNickname, LocalDateTime lastReadDate) {
        this.title = title;
        this.authorNickname = authorNickname;
        this.lastReadDate = lastReadDate;
    }
}
