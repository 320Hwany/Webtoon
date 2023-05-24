package com.webtoon.contentmember.dto.requeset;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentMemberSave {

    private Long contentId;

    private Long memberId;

    @Builder
    private ContentMemberSave(Long contentId, Long memberId) {
        this.contentId = contentId;
        this.memberId = memberId;
    }

    public static ContentMemberSave toContentMemberSave(Long contentId, Long memberId) {
        return ContentMemberSave.builder()
                .contentId(contentId)
                .memberId(memberId)
                .build();
    }
}
