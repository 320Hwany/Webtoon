package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CartoonRepository {

    Cartoon save(Cartoon cartoon);

    Cartoon getById(Long id);

    List<Cartoon> findAllByTitle(CartoonSearch cartoonSearch);

    List<Cartoon> findAllByGenre(CartoonSearch cartoonSearch);

    List<Cartoon> findAllOrderByLikes(CartoonSearch cartoonSearch);

    List<Cartoon> findAllByAuthornickname(CartoonSearch cartoonSearch);

    void delete(Cartoon cartoon);

    void saveAll(List<Cartoon> cartoonList);

    long count();
}
