package com.webtoon.cartoon.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartoonCore {

    private String title;

    private String nickname;
    private long likes;

    @Builder
    @QueryProjection
    public CartoonCore(String title, String nickname, long likes) {
        this.title = title;
        this.nickname = nickname;
        this.likes = likes;
    }
}
