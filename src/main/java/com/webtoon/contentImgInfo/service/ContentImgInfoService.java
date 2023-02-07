package com.webtoon.contentImgInfo.service;

import com.webtoon.content.domain.Content;
import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import com.webtoon.contentImgInfo.exception.ContentImgInfoNotFoundException;
import com.webtoon.contentImgInfo.repository.ContentImgInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
public class ContentImgInfoService {

    private final ContentImgInfoRepository contentImgInfoRepository;

    public void imgUploadOnServer(MultipartFile multipartFile, String imgDir) throws IOException {
        ContentImgInfo.imgUploadOnServer(multipartFile, imgDir);
    }

    @Transactional
    public void saveContentImgInfo(MultipartFile multipartFile, Content content) {
        ContentImgInfo contentImgInfo = ContentImgInfo.makeContentImgInfo(multipartFile, content);
        contentImgInfoRepository.save(contentImgInfo);
    }

    public ContentImgInfo getByContentId(Long contentId) {
        return contentImgInfoRepository.getByContentId(contentId)
                .orElseThrow(ContentImgInfoNotFoundException::new);
    }

    public UrlResource getImgFromServer(ContentImgInfo contentImgInfo, String imgDir) throws MalformedURLException {
        return new UrlResource("file:" + imgDir + contentImgInfo.getImgName());
    }

    public MediaType getMediaType(ContentImgInfo contentImgInfo, String imgDir) throws IOException {
        return MediaType.parseMediaType(
                Files.probeContentType(Paths.get("file:" + imgDir + contentImgInfo.getImgName())));
    }
}

