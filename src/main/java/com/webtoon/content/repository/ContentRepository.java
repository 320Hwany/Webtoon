package com.webtoon.content.repository;

import com.webtoon.content.domain.Content;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository {

    Content save(Content content);

    Optional<Content> findByCartoonAndEpisode(Long cartoonId, Integer episode);
}
