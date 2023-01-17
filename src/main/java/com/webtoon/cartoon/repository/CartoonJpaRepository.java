package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartoonJpaRepository extends JpaRepository<Cartoon, Long> {

    Optional<Cartoon> findByTitle(String title);
}
