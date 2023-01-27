package com.webtoon.contentimg.repository;

import com.webtoon.contentimg.domain.ContentImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentImgJpaRepository extends JpaRepository<ContentImg, Long> {
}
