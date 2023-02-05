package com.webtoon.content.repository;

import com.webtoon.content.domain.Content;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ContentRepository {

    Content save(Content content);

    Content getById(Long id);

    Optional<Content> findByCartoonIdAndEpisode(Long cartoonId, Long episode);
}
