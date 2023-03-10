package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSearchTitle;

import java.util.List;

public interface CartoonRepository {

    Cartoon save(Cartoon cartoon);

    Cartoon getById(Long id);

    List<Cartoon> findAllByTitle(CartoonSearchTitle cartoonSearchTitle);

    List<Cartoon> findAllByCartoonCondOrderByLikes(CartoonSearch cartoonSearch);

    List<Cartoon> findAllByCartoonCondOrderByRating(CartoonSearch cartoonSearch);

    void delete(Cartoon cartoon);

    void deleteAll();

    void saveAll(List<Cartoon> cartoonList);

    long count();
}
