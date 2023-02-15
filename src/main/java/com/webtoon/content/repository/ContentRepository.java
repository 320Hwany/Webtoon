package com.webtoon.content.repository;

import com.webtoon.content.domain.Content;

import java.util.Optional;

public interface ContentRepository {

    Content save(Content content);

    Content getById(Long id);

    Optional<Content> findByCartoonIdAndEpisode(Long cartoonId, int episode);
}
