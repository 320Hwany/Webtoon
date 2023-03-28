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


import static com.webtoon.contentImgInfo.domain.ContentImgInfo.*;

@RequiredArgsConstructor
@Service
public class ContentImgInfoService {

    private final ContentImgInfoRepository contentImgInfoRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public void save(Long contentId, MultipartFile uploadImg) {
        Content content = contentRepository.getById(contentId);
        ContentImgInfo contentImgInfo = toContentImgInfo(uploadImg, content);
        contentImgInfoRepository.save(contentImgInfo);
    }

    public void imgUploadOnServer(MultipartFile uploadImg, String imgDir) {
        ContentImgInfo.imgUploadOnServer(uploadImg, imgDir);
    }

    public ContentImgInfo getByContentId(Long contentId) {
        return contentImgInfoRepository.findByContentId(contentId)
                .orElseThrow(ContentImgInfoNotFoundException::new);
    }

    public UrlResource getImgFromServer(ContentImgInfo contentImgInfo, String imgDir) {
        return contentImgInfo.getImgFromServer(contentImgInfo, imgDir);
    }

    public MediaType getMediaType(ContentImgInfo contentImgInfo, String imgDir) {
        return contentImgInfo.getMediaType(contentImgInfo, imgDir);
    }
}

