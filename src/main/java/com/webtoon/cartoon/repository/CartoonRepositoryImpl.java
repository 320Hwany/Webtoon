package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CartoonRepositoryImpl implements CartoonRepository {

    private final CartoonJpaRepository cartoonJpaRepository;

    @Override
    public void save(Cartoon cartoon) {
        cartoonJpaRepository.save(cartoon);
    }
}
