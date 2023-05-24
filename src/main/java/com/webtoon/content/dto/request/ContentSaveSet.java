package com.webtoon.content.dto.request;

import com.webtoon.author.domain.AuthorSession;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentSaveSet {

    private AuthorSession authorSession;

    private Long cartoonId;

    private ContentSave contentSave;

    @Builder
    private ContentSaveSet(AuthorSession authorSession, Long cartoonId, ContentSave contentSave) {
        this.authorSession = authorSession;
        this.cartoonId = cartoonId;
        this.contentSave = contentSave;
    }

    public static ContentSaveSet toContentSaveSet(AuthorSession authorSession, Long cartoonId, ContentSave contentSave) {
        return ContentSaveSet.builder()
                .authorSession(authorSession)
                .cartoonId(cartoonId)
                .contentSave(contentSave)
                .build();
    }
}
