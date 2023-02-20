package com.webtoon.contentImgInfo.controller;

import com.webtoon.content.domain.Content;
import com.webtoon.content.service.ContentService;
import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import com.webtoon.contentImgInfo.service.ContentImgInfoService;
import com.webtoon.contentImgInfo.service.ContentImgInfoTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ContentImgInfoController {

    private final ContentImgInfoService contentImgInfoService;
    private final ContentImgInfoTransactionService contentImgInfoTransactionService;

    @Value("${file.dir}")
    private String imgDir;

    @PostMapping("/contentImg/{contentId}")
    public ResponseEntity<Void> save(@RequestParam MultipartFile multipartFile,
                                     @PathVariable Long contentId) throws IOException {

        contentImgInfoService.imgUploadOnServer(multipartFile, imgDir);
        contentImgInfoTransactionService.saveTransactionSet(contentId, multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contentImg/{contentId}")
    public ResponseEntity<UrlResource> getContentImg(@PathVariable Long contentId) throws IOException {
        ContentImgInfo contentImgInfo = contentImgInfoService.getByContentId(contentId);
        UrlResource contentImg = contentImgInfoService.getImgFromServer(contentImgInfo, imgDir);
        MediaType mediaType = contentImgInfoService.getMediaType(contentImgInfo, imgDir);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(contentImg);
    }
}
