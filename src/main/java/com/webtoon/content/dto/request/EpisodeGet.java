package com.webtoon.content.dto.request;

import lombok.*;

import javax.validation.constraints.Min;

import static com.webtoon.util.constant.ConstantValid.CARTOON_SIZE_VALID_MESSAGE;
import static com.webtoon.util.constant.ConstantValid.PAGE_VALID_MESSAGE;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EpisodeGet {

    @Min(value = 1, message = PAGE_VALID_MESSAGE)
    private int page;

    @Min(value = 0, message = CARTOON_SIZE_VALID_MESSAGE)
    private int size;

    private Long cartoonId;

    private int contentEpisode;

    @Builder
    public EpisodeGet(int page, int size, Long cartoonId, int contentEpisode) {
        this.page = page;
        this.size = size;
        this.cartoonId = cartoonId;
        this.contentEpisode = contentEpisode;
    }
}
