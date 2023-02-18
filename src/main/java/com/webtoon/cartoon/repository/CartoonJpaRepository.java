package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartoonJpaRepository extends JpaRepository<Cartoon, Long> {


    @EntityGraph(attributePaths = {"static/author"})
    Page<Cartoon> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"static/author"})
    List<Cartoon> findAllByGenre(Genre genre, Pageable pageable);

    @EntityGraph(attributePaths = {"static/author"})
    List<Cartoon> findAllByTitleContains(String title, Pageable pageable);

    @EntityGraph(attributePaths = {"static/author"})
    List<Cartoon> findAllByDayOfTheWeek(DayOfTheWeek dayOfTheWeek, Pageable pageable);
}
