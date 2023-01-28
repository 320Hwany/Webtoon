package com.webtoon.contentimg.repository;

import com.webtoon.contentimg.domain.ContentImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ContentImgRepositoryImpl implements ContentImgRepository {

    private final ContentImgJpaRepository contentImgJpaRepository;


    @Override
    public void save(ContentImg contentImg) {
        contentImgJpaRepository.save(contentImg);
    }

    @Override
    public long count() {
        return contentImgJpaRepository.count();
    }
}
