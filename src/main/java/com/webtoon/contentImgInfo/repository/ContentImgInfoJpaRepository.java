package com.webtoon.contentImgInfo.repository;


import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentImgInfoJpaRepository extends JpaRepository<ContentImgInfo, Long> {
}
