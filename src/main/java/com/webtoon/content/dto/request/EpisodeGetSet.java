package com.webtoon.content.dto.request;

import com.webtoon.member.domain.MemberSession;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EpisodeGetSet {

    private Long memberSessionId;
    private Long cartoonId;
    private int contentEpisode;

    @Builder
    public EpisodeGetSet(Long memberSessionId, Long cartoonId, int contentEpisode) {
        this.memberSessionId = memberSessionId;
        this.cartoonId = cartoonId;
        this.contentEpisode = contentEpisode;
    }

    public static EpisodeGetSet toEpisodeGetSet(MemberSession memberSession, EpisodeGet episodeGet) {
        return EpisodeGetSet.builder()
                .memberSessionId(memberSession.getId())
                .cartoonId(episodeGet.getCartoonId())
                .contentEpisode(episodeGet.getContentEpisode())
                .build();
    }
}
