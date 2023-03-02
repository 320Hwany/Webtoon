package com.webtoon.contentImgInfo.application;

import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
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
    private final ContentRepository contentRepository;

    @Transactional
    public void saveSet(Long contentId, MultipartFile multipartFile) {
        Content content = contentRepository.getById(contentId);
        ContentImgInfo contentImgInfo = ContentImgInfo.makeContentImgInfo(multipartFile, content);
        contentImgInfoRepository.save(contentImgInfo);
    }

    public void imgUploadOnServer(MultipartFile multipartFile, String imgDir) throws IOException {
        ContentImgInfo.imgUploadOnServer(multipartFile, imgDir);
    }

    public ContentImgInfo getByContentId(Long contentId) {
        return contentImgInfoRepository.findByContentId(contentId)
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

