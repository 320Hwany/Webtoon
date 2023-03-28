package com.webtoon.contentImgInfo.presentation;

import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import com.webtoon.contentImgInfo.application.ContentImgInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ContentImgInfoController {

    private final ContentImgInfoService contentImgInfoService;

    @Value("${file.dir}")
    private String imgDir;

    @PostMapping("/contentImg/{contentId}")
    public ResponseEntity<Void> save(@RequestParam MultipartFile uploadImg,
                                     @PathVariable Long contentId) {
        contentImgInfoService.imgUploadOnServer(uploadImg, imgDir);
        contentImgInfoService.save(contentId, uploadImg);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/contentImg")
    public ResponseEntity<UrlResource> getContentImg(@RequestParam Long contentId) {
        ContentImgInfo contentImgInfo = contentImgInfoService.getByContentId(contentId);
        UrlResource contentImg = contentImgInfoService.getImgFromServer(contentImgInfo, imgDir);
        MediaType mediaType = contentImgInfoService.getMediaType(contentImgInfo, imgDir);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(contentImg);
    }
}
