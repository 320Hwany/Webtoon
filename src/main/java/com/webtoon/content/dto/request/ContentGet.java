package com.webtoon.content.dto.request;

import lombok.*;

import javax.validation.constraints.Min;

import static com.webtoon.util.constant.ConstantValid.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentGet {

    @Min(value = 1, message = PAGE_VALID_MESSAGE)
    private int page;

    @Min(value = 0, message = CONTENT_SIZE_VALID_MESSAGE)
    private int size;

    private Long cartoonId;

    @Builder
    private ContentGet(int page, int size, Long cartoonId) {
        this.page = page;
        this.size = size;
        this.cartoonId = cartoonId;
    }

    public static ContentGet toContentGet(int page, int size, Long cartoonId) {
        return ContentGet.builder()
                .page(page)
                .size(size)
                .cartoonId(cartoonId)
                .build();
    }

    public int getOffset() {
        return Math.max(page - 1, 0) * size;
    }
}
