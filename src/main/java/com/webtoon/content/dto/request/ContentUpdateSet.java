package com.webtoon.content.dto.request;

import com.webtoon.author.domain.AuthorSession;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentUpdateSet {

    private AuthorSession authorSession;
    private Long cartoonId;

    private int contentEpisode;

    private ContentUpdate contentUpdate;

    @Builder
    private ContentUpdateSet(AuthorSession authorSession, Long cartoonId,
                            int contentEpisode, ContentUpdate contentUpdate) {
        this.authorSession = authorSession;
        this.cartoonId = cartoonId;
        this.contentEpisode = contentEpisode;
        this.contentUpdate = contentUpdate;
    }

    public static ContentUpdateSet toContentUpdateSet(AuthorSession authorSession, Long cartoonId,
                                                      int contentEpisode, ContentUpdate contentUpdate) {
        return ContentUpdateSet.builder()
                .authorSession(authorSession)
                .cartoonId(cartoonId)
                .contentEpisode(contentEpisode)
                .contentUpdate(contentUpdate)
                .build();
    }
}
