package com.webtoon.content.repository;

import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentGet;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContentRepository {

    Content save(Content content);

    Content getById(Long id);

    List<Content> findAllForCartoon(ContentGet contentGet);

    Optional<Content> findByCartoonIdAndEpisode(Long cartoonId, int episode);

    void saveAll(List<Content> contentList);

    void deleteAll();

    long count();
}
