package com.webtoon.content.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ContentGet {

    private Long memberSessionId;
    private Long cartoonId;
    private int contentEpisode;

    @Builder
    public ContentGet(Long cartoonId, int contentEpisode, Long memberSessionId) {
        this.cartoonId = cartoonId;
        this.contentEpisode = contentEpisode;
        this.memberSessionId = memberSessionId;
    }

    public static ContentGet getFromIdAndEpisode(Long memberSessionId, Long cartoonId, int contentEpisode) {
        return ContentGet.builder()
                .memberSessionId(memberSessionId)
                .cartoonId(cartoonId)
                .contentEpisode(contentEpisode)
                .build();
    }
}
