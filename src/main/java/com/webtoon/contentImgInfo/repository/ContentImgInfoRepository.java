package com.webtoon.contentImgInfo.repository;

import com.webtoon.contentImgInfo.domain.ContentImgInfo;

import java.util.Optional;

public interface ContentImgInfoRepository {

    void save(ContentImgInfo contentImgInfo);

    Optional<ContentImgInfo> findByContentId(Long contentId);

    long count();
}
