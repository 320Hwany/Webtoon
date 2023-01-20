package com.webtoon.content.repository;

import com.webtoon.content.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentJpaRepository extends JpaRepository<Content, Long> {

    Optional<Content> findById(Long id);
}
