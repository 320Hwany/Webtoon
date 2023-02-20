package com.webtoon.content.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ContentUpdateSet {

    private Long cartoonId;
    private int contentEpisode;
    private ContentUpdate contentUpdate;

    @Builder
    public ContentUpdateSet(Long cartoonId, int contentEpisode, ContentUpdate contentUpdate) {
        this.cartoonId = cartoonId;
        this.contentEpisode = contentEpisode;
        this.contentUpdate = contentUpdate;
    }

    public static ContentUpdateSet getFromIdAndEpisode(Long cartoonId, int contentEpisode,
                                                       ContentUpdate contentUpdate) {
        return ContentUpdateSet.builder()
                .cartoonId(cartoonId)
                .contentEpisode(contentEpisode)
                .contentUpdate(contentUpdate)
                .build();
    }
}
