package com.webtoon.content.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ContentImg {

    private String url;

    private MultipartFile contentImg;
}
