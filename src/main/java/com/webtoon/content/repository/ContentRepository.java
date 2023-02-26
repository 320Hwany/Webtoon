package com.webtoon.content.repository;

import com.webtoon.content.domain.Content;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContentRepository {

    Content save(Content content);

    Content getById(Long id);

    List<Content> findAllByCartoonId(Long cartoonId, Pageable pageable);

    Optional<Content> findByCartoonIdAndEpisode(Long cartoonId, int episode);

    void deleteAll();

    long count();
}
