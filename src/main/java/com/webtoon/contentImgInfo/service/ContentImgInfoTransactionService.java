package com.webtoon.contentImgInfo.service;

import com.webtoon.content.domain.Content;
import com.webtoon.content.service.ContentService;
import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import com.webtoon.contentImgInfo.repository.ContentImgInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContentImgInfoTransactionService {

    private final ContentImgInfoRepository contentImgInfoRepository;
    private final ContentService contentService;

    @Transactional
    public void saveTransactionSet(Long contentId, MultipartFile multipartFile) {
        Content content = contentService.getById(contentId);
        ContentImgInfo contentImgInfo = ContentImgInfo.makeContentImgInfo(multipartFile, content);
        contentImgInfoRepository.save(contentImgInfo);
    }
}
