package com.webtoon.content.repository;

import com.webtoon.content.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentJpaRepository extends JpaRepository<Content, Long> {
}
