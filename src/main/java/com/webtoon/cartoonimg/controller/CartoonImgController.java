package com.webtoon.cartoonimg.controller;

import com.webtoon.cartoonimg.service.CartoonImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class CartoonImgController {

    private final CartoonImgService cartoonImgService;

    @PostMapping( "/cartoonImg")
    public void save(@RequestParam MultipartFile multipartFiles[]) throws IOException {
        cartoonImgService.imgUpload(multipartFiles);
    }
}
