package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartoonJpaRepository extends JpaRepository<Cartoon, Long> {
}
