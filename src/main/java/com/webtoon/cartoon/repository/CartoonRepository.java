package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.util.enumerated.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartoonRepository {

    Cartoon save(Cartoon cartoon);

    Cartoon getById(Long id);

    List<Cartoon> findAllByTitle(CartoonSearch cartoonSearch);

    List<Cartoon> findAllByGenre(CartoonSearch cartoonSearch);

    List<Cartoon> findAllOrderByLikes(CartoonSearch cartoonSearch);

    void delete(Cartoon cartoon);

    void saveAll(List<Cartoon> cartoonList);

    Long count();
}
