package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.util.enumerated.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartoonRepository {

    Cartoon save(Cartoon cartoon);

    Cartoon getById(Long id);

    Cartoon getByTitle(String title);

    List<Cartoon> findAllByGenre(Genre genre);
}
