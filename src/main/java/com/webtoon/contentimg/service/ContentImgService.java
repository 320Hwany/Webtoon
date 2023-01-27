package com.webtoon.contentimg.service;

import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.contentimg.domain.ContentImg;
import com.webtoon.contentimg.repository.ContentImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ContentImgService {

    private final ContentImgRepository contentImgRepository;

    @Value("${file.dir}")
    private String imgDir;

    public void imgUploadOnServer(MultipartFile multipartFiles[]) throws IOException {
        ContentImg.imgUploadOnServer(multipartFiles, imgDir);
    }

    @Transactional
    public void makeContentImg(MultipartFile multipartFiles[], Content content) {
        ContentImg contentImg = ContentImg.makeContentImg(multipartFiles, content);
        contentImgRepository.save(contentImg);
    }
}

