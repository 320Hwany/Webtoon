package com.webtoon.content.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentGet {

    private Long memberSessionId;
    private Long cartoonId;
    private int contentEpisode;

    public static ContentGet getFromIdAndEpisode(Long memberSessionId, Long cartoonId, int contentEpisode) {
        return ContentGet.builder()
                .memberSessionId(memberSessionId)
                .cartoonId(cartoonId)
                .contentEpisode(contentEpisode)
                .build();
    }
}
