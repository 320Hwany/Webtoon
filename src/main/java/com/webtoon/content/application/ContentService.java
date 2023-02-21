package com.webtoon.content.application;

import com.webtoon.content.domain.Content;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContentService {

    private final ContentRepository contentRepository;

    public Content getById(Long id) {
        return contentRepository.getById(id);
    }

    public Content findByCartoonIdAndEpisode(Long cartoonId, int episode) {
        return contentRepository.findByCartoonIdAndEpisode(cartoonId, episode)
                .orElseThrow(ContentNotFoundException::new);
    }
}
