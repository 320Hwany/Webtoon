package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.*;


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
    public List<Cartoon> findAllByTitle(CartoonSearch cartoonSearch) {
        PageRequest pageRequest = PageRequest.of(cartoonSearch.getPage(), cartoonSearch.getLimit(),
                Sort.by(DESC, "id"));
        List<Cartoon> cartoonList =
                cartoonJpaRepository.findAllByTitleContains(cartoonSearch.getTitle(), pageRequest);
        return cartoonList;
    }

    @Override
    public List<Cartoon> findAllByGenre(CartoonSearch cartoonSearch) {
        PageRequest pageRequest = PageRequest.of(cartoonSearch.getPage(), cartoonSearch.getLimit(),
                Sort.by(DESC, "id"));
        return cartoonJpaRepository.findAllByGenre(cartoonSearch.getGenre(), pageRequest);
    }

    @Override
    public List<Cartoon> findAllOrderByLikes(CartoonSearch cartoonSearch) {
        PageRequest pageRequest = PageRequest.of(cartoonSearch.getPage(), cartoonSearch.getLimit(),
                Sort.by(DESC, "likes"));
        Page<Cartoon> cartoonPage = cartoonJpaRepository.findAll(pageRequest);
        List<Cartoon> cartoonList = cartoonPage.getContent();
        return cartoonList;
    }

    @Override
    public void delete(Cartoon cartoon) {
        cartoonJpaRepository.delete(cartoon);
    }

    @Override
    public void saveAll(List<Cartoon> cartoonList) {
        cartoonJpaRepository.saveAll(cartoonList);
    }

    @Override
    public long count() {
        return cartoonJpaRepository.count();
    }
}
