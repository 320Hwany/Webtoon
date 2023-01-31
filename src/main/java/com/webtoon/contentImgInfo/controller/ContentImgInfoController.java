package com.webtoon.contentImgInfo.controller;

import com.webtoon.content.domain.Content;
import com.webtoon.content.service.ContentService;
import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import com.webtoon.contentImgInfo.service.ContentImgInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RequiredArgsConstructor
@RestController
public class ContentImgInfoController {

    private final ContentImgInfoService contentImgInfoService;
    private final ContentService contentService;

    @Value("${file.dir}")
    private String imgDir;

    @PostMapping("/contentImg/{contentId}")
    public ResponseEntity<Void> save(@RequestParam MultipartFile multipartFile,
                                     @PathVariable Long contentId) throws IOException {

        Content content = contentService.getById(contentId);
        contentImgInfoService.imgUploadOnServer(multipartFile, imgDir);
        contentImgInfoService.saveContentImgInfo(multipartFile, content);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contentImg/{contentId}")
    public ResponseEntity<UrlResource> getContentImg(@PathVariable Long contentId) throws MalformedURLException {
        ContentImgInfo contentImgInfo = contentImgInfoService.getByContentId(contentId);
        UrlResource contentImg = contentImgInfoService.getImgFromServer(contentImgInfo, imgDir);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(contentImg);
    }
}
