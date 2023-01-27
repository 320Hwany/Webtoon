package com.webtoon.contentimg.controller;

import com.webtoon.content.domain.Content;
import com.webtoon.content.service.ContentService;
import com.webtoon.contentimg.service.ContentImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ContentImgController {

    private final ContentImgService contentImgService;
    private final ContentService contentService;

    @PostMapping("/cartoonImg/{contentId}")
    public ResponseEntity<Void> save(@RequestParam MultipartFile multipartFiles[],
                                     @PathVariable Long contentId) throws IOException {

        Content content = contentService.getById(contentId);
        contentImgService.imgUploadOnServer(multipartFiles);
        contentImgService.makeContentImg(multipartFiles, content);
        return ResponseEntity.ok().build();
    }
}
