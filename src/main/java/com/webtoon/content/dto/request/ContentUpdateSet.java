package com.webtoon.content.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentUpdateSet {

    private Long cartoonId;
    private int contentEpisode;
    private ContentUpdate contentUpdate;

    public static ContentUpdateSet getFromIdAndEpisode(Long cartoonId, int contentEpisode,
                                                       ContentUpdate contentUpdate) {
        return ContentUpdateSet.builder()
                .cartoonId(cartoonId)
                .contentEpisode(contentEpisode)
                .contentUpdate(contentUpdate)
                .build();
    }
}
