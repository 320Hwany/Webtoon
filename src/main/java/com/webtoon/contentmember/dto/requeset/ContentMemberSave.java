package com.webtoon.contentmember.dto.requeset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentMemberSave {

    private Long contentId;

    private Long memberId;

    public static ContentMemberSave getFromContentIdAndMemberId(Long contentId, Long memberId) {
        return ContentMemberSave.builder()
                .contentId(contentId)
                .memberId(memberId)
                .build();
    }
}
