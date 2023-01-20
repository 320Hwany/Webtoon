package com.webtoon.content.service;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
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
    private final CartoonRepository cartoonRepository;

    @Transactional
    public Content save(Content content) {
        return contentRepository.save(content);
    }

    @Transactional
    public void update(Content content, ContentUpdate contentUpdate) {
        content.update(contentUpdate);
    }

    public Content getContentFromContentSaveAndCartoonId(ContentSave contentSave, Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        Content content = Content.getFromContentSaveAndCartoon(contentSave, cartoon);
        return content;
    }

    public Content findByCartoonIdAndEpisode(Long cartoonId, Integer episode) {
        return contentRepository.findByCartoonIdAndEpisode(cartoonId, episode)
                .orElseThrow(ContentNotFoundException::new);
    }
}
