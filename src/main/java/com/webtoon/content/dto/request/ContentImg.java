package com.webtoon.content.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentImg {

    private MultipartFile contentImg;

    @Builder
    private ContentImg(MultipartFile contentImg) {
        this.contentImg = contentImg;
    }
}
