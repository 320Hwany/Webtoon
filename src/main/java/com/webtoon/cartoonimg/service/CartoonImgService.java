package com.webtoon.cartoonimg.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class CartoonImgService {

    @Value("/home/ec2-user/webtoon/")
    private String imgDir;

    public void imgUpload(MultipartFile[] multipartFiles) throws IOException {
        for (MultipartFile file : multipartFiles) {
            if (!file.isEmpty()) {
                String fullPath = imgDir + file.getOriginalFilename();
                file.transferTo(new File(fullPath));
            }
        }
    }
}

