package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CartoonRepositoryImpl implements CartoonRepository {

    private final CartoonJpaRepository cartoonJpaRepository;

    @Override
    public Cartoon save(Cartoon cartoon) {
        return cartoonJpaRepository.save(cartoon);
    }

    @Override
    public Cartoon getById(Long id) {
        return cartoonJpaRepository.findById(id)
                .orElseThrow(CartoonNotFoundException::new);
    }

    @Override
    public Cartoon getByTitle(String title) {
        return cartoonJpaRepository.findByTitle(title)
                .orElseThrow(CartoonNotFoundException::new);
    }

    @Override
    public void deleteAll() {
        cartoonJpaRepository.deleteAll();
    }
}
