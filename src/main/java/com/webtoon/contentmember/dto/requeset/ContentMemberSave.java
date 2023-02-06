package com.webtoon.contentmember.dto.requeset;

import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ContentMemberSave {

    private Long contentId;

    private Long memberId;

    @Builder
    public ContentMemberSave(Long contentId, Long memberId) {
        this.contentId = contentId;
        this.memberId = memberId;
    }

    public static ContentMemberSave getFromContentIdAndMemberId(Long contentId, Long memberId) {
        return ContentMemberSave.builder()
                .contentId(contentId)
                .memberId(memberId)
                .build();
    }
}
